package hr.fer.zemris.projekt.algorithms.neural.elman.ga;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.neural.ActivationFunction;
import hr.fer.zemris.projekt.algorithms.neural.Utils;
import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.Stats;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 9.12.2016..
 */
public class GA extends ObservableAlgorithm {
    private ThreadLocal<ElmanNeuralNetwork> network;
    private AbstractSimulator simulator;

    @Override
    public Robot readSolutionFromFile(Path filePath) throws IOException {
        return null;
    }

    @Override
    public void writeSolutionToFile(Path filePath, Robot robot) throws IOException {
        Objects.requireNonNull(robot, "Robot cannot be null.");
        if (!(robot instanceof ElmanNeuralNetwork)) {
            throw new IllegalArgumentException("Given implementation is not correct. Expected Elman neural network.");
        }

        StringJoiner sj = new StringJoiner(" ");
        for (double weight : ((ElmanNeuralNetwork) robot).getWeights()) {
            sj.add(Double.toString(weight));
        }

        Files.write(filePath, sj.toString().getBytes());
    }

    @Override
    public Parameters<? extends Algorithm> getDefaultParameters() {
        return new GAParameters();
    }

    @Override
    public void run(
            AbstractSimulator simulator, Parameters<? extends Algorithm> parameters) {
        if (!(parameters instanceof GAParameters)) {
            throw new IllegalStateException("Cannot pass non-GA parameters to GA class.");
        }
        this.simulator = simulator;
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        network = ThreadLocal.withInitial(() -> new ElmanNeuralNetwork(new int[] {
                15, 15, 7 }, new ActivationFunction[] { ActivationFunction.SIGMOID, ActivationFunction.SIGMOID }));
        int chromosomeSize = network.get().getNumberOfWeights();

        //loading parameters
        int populationSize = (int) parameters.getParameter(GAParameters.POPULATION_SIZE).getValue();
        int maxGenerations = (int) parameters.getParameter(GAParameters.MAX_GENERATIONS).getValue();
        int tournamentSize = (int) parameters.getParameter(GAParameters.TOURNAMENT_SIZE).getValue();
        double selectionProbability = parameters.getParameter(GAParameters.SELECTION_PROBABILITY).getValue();
        double alpha = parameters.getParameter(GAParameters.ALPHA).getValue();
        double mutationRange = parameters.getParameter(GAParameters.MUTATION_RANGE).getValue();
        double mutationRate = parameters.getParameter(GAParameters.MUTATION_RATE).getValue();
        double refreshRate = parameters.getParameter(GAParameters.REFRESH_RATE).getValue();

        List<Chromosome> population = initializePopulation(populationSize, chromosomeSize);

        for (int i = 0; i < maxGenerations; i++) {
            if (i % 100 == 0) {
                simulator.generateGrids(100, 50, 10, 10, false);
            }
            evaluatePopulation(population, pool);

            population = sortPopulation(population);
            printBest(population, i);

            List<Chromosome> newPopulation = new ArrayList<>();

            newPopulation.add(population.get(0));
            newPopulation.add(population.get(1));

            while (newPopulation.size() < populationSize) {
                Pair parents = selectParents(population, tournamentSize, selectionProbability);
                Pair children = crossover(parents, alpha);

                mutate(children.first, mutationRate, mutationRange);
                mutate(children.second, mutationRate, mutationRange);

                newPopulation.add(children.first);
                newPopulation.add(children.second);
            }

            population = newPopulation;
        }

        population = sortPopulation(population);
        Chromosome best = population.get(0);

        simulator.generateGrids(1000, 50, 10, 10, false);
        evaluateSolution(best);
        System.out.println(best);

        pool.shutdown();
    }

    private void printBest(List<Chromosome> population, int iteration) {
        if(iteration % 10 == 0) {
            System.out.println("Iteration " + iteration + ", " + population.get(0).toString());
        }
    }

    private void mutate(Chromosome child, double mutationRate, double mutationRange) {
        double[] chromosome = child.getWeights();
        for (int i = 0; i < chromosome.length; i++) {
            chromosome[i] += Utils.RANDOM.nextGaussian() * 0.5;
        }
    }

    private Pair crossover(Pair parents, double alpha) {
        double[] firstParent = parents.first.getWeights();
        double[] secondParent = parents.second.getWeights();

        double[] firstChild = new double[firstParent.length];
        double[] secondChild = new double[secondParent.length];

        for(int i = 0; i < firstChild.length; i++) {
            double min = Math.min(firstParent[i], secondParent[i]);
            double max = Math.max(firstParent[i], secondParent[i]);
            double delta = max - min;

            firstChild[i] = Utils.RANDOM.nextDouble() * delta * (2 * alpha + 1) + min - delta * alpha;
            secondChild[i] = Utils.RANDOM.nextDouble() * delta * (2 * alpha + 1) + min - delta * alpha;
        }

        return new Pair(new Chromosome(firstChild), new Chromosome(secondChild));
    }

    private Pair selectParents(List<Chromosome> population, int tournamentSize, double selectionProbability) {
        Chromosome first = tournamentSelection(population, tournamentSize, selectionProbability);
        Chromosome second = tournamentSelection(population, tournamentSize, selectionProbability);

        return new Pair(first, second);
    }

    private Chromosome tournamentSelection(
            List<Chromosome> population, int tournamentSize, double selectionProbability) {
        List<Chromosome> chosen = new ArrayList<>();
        int populationSize = population.size();

        int size = 0;
        while (size < tournamentSize) {
            Chromosome chromosome = population.get(Utils.RANDOM.nextInt(populationSize));
            if (chosen.add(chromosome)) {
                size++;
            }
        }
        chosen = sortPopulation(chosen);

        Map<Chromosome, Double> probabilities = new LinkedHashMap<>();
        probabilities.put(chosen.get(0), selectionProbability);

        double total = selectionProbability;
        double rest = 1 - selectionProbability;
        for (int i = 1, n = chosen.size() - 1; i < n; i++) {
            double probability = selectionProbability * Math.pow(rest, i);
            probabilities.put(chosen.get(i), probability);
            total += probability;
        }

        probabilities.put(chosen.get(chosen.size() - 1), 1 - total);

        double random = Utils.RANDOM.nextDouble();
        total = 0;
        for(Map.Entry<Chromosome, Double> entry : probabilities.entrySet()) {
            total += entry.getValue();
            if (random <= total) {
                return entry.getKey();
            }
        }

        return chosen.get(0);
    }

    private List<Chromosome> sortPopulation(List<Chromosome> population) {
        return population.stream().sorted((c1, c2) -> -Double.compare(c1.getFitness(), c2.getFitness()))
                .collect(Collectors.toList());
    }

    private void evaluatePopulation(List<Chromosome> population, ExecutorService pool) {
        List<Callable<Void>> callables = new ArrayList<>();
        population.forEach(c -> callables.add(() -> evaluateSolution(c)));

        try {
            List<Future<Void>> results = pool.invokeAll(callables);

            for (Future<Void> result : results) {
                result.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Void evaluateSolution(Chromosome solution) {
        network.get().clearContext();
        network.get().setWeights(solution.getWeights());

        List<Stats> stats = simulator.playGames(network.get());
        solution.setFitness(processStats(stats));
        return null;
    }

    private double processStats(List<Stats> stats) {
        double total = 0;
        for (Stats stat : stats) {
            total += 10 * stat.getBottlesCollected() - 1 * stat.getEmptyPickups() - 5 * stat.getWallsHit();
        }

        return total / stats.size();
    }

    private List<Chromosome> initializePopulation(int populationSize, int chromosomeSize) {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Chromosome(chromosomeSize));
        }

        return population;
    }

    private static class Pair {
        public Chromosome first;
        public Chromosome second;

        public Pair(Chromosome first, Chromosome second) {
            this.first = first;
            this.second = second;
        }
    }
}
