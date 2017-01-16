package hr.fer.zemris.projekt.algorithms.neural.elman.ga;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.neural.ActivationFunction;
import hr.fer.zemris.projekt.algorithms.neural.elman.ElmanNeuralNetwork;
import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.Stats;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 9.12.2016..
 */
public class GA extends ObservableAlgorithm {
    private ThreadLocal<ElmanNeuralNetwork> network;
    private AbstractSimulator simulator;

    @Override
    public Robot readSolutionFromFile(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);

        int[] numberOfNeurons = Arrays.stream(lines.get(0).split("x")).mapToInt(Integer::parseInt).toArray();
        ActivationFunction[] functions = Arrays.stream(lines.get(1).split(" ")).map(ActivationFunction::valueOf)
                .toArray(ActivationFunction[]::new);
        double[] weights = Arrays.stream(lines.get(2).split(" ")).mapToDouble(Double::parseDouble).toArray();

        ElmanNeuralNetwork enn = new ElmanNeuralNetwork(numberOfNeurons, functions);
        enn.setWeights(weights);

        return enn;
    }

    @Override
    public void writeSolutionToFile(Path filePath, Robot robot) throws IOException {
        Objects.requireNonNull(robot, "Robot cannot be null.");
        if (!(robot instanceof ElmanNeuralNetwork)) {
            throw new IllegalArgumentException(
                    "Given implementation is not correct. Expected Elman neural network, " + "given: " +
                    robot.getClass());
        }

        ElmanNeuralNetwork r = (ElmanNeuralNetwork) robot;
        String prefix = r.getArchitecture() + "\n" + r.getFunctions() + "\n";

        StringJoiner sj = new StringJoiner(" ", prefix, "");
        for (double weight : r.getWeights()) {
            sj.add(Double.toString(weight));
        }

        Files.write(filePath, sj.toString().getBytes());
    }

    @Override
    public Parameters<? extends Algorithm> getDefaultParameters() {
        return new GAParameters();
    }

    @Override
    public Robot run(
            AbstractSimulator simulator, Parameters<? extends Algorithm> parameters) {
        if (!(parameters instanceof GAParameters)) {
            throw new IllegalStateException("Cannot pass non-GA parameters to GA class.");
        }
        this.simulator = simulator;
        ExecutorService pool = null;

        try {
            pool = Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors() + 1,
                    r -> {
                        Thread t = new Thread(r);
                        t.setDaemon(true);
                        return t;
                    }
            );
            network = ThreadLocal.withInitial(() -> new ElmanNeuralNetwork(new int[]{
                    15, 7, 7}, new ActivationFunction[]{ActivationFunction.HYP_TAN, ActivationFunction.HYP_TAN}));
            int chromosomeSize = network.get().getNumberOfWeights();

            //loading parameters
            int populationSize = (int) parameters.getParameter(GAParameters.POPULATION_SIZE).getValue();
            int maxGenerations = (int) parameters.getParameter(GAParameters.MAX_GENERATIONS).getValue();
            int tournamentSize = (int) parameters.getParameter(GAParameters.TOURNAMENT_SIZE).getValue();
            double alpha = parameters.getParameter(GAParameters.ALPHA).getValue();
            double sigma = parameters.getParameter(GAParameters.SIGMA).getValue();
            double stopCondition = parameters.getParameter(GAParameters.STOP_CONDITION).getValue();

            List<Chromosome> population = initializePopulation(populationSize, chromosomeSize);

            for (int i = 0; i < maxGenerations; i++) {

                if (i % 100 == 0) {
                    simulator.generateGrids(30, 10, 10, false, ThreadLocalRandom.current());
                }

                evaluatePopulation(population, pool);
                population = sortPopulation(population);
                if (population.get(0).getFitness() > stopCondition) {
                    break;
                }

                ElmanNeuralNetwork best = prepareBest(population.get(0));
                this.notifyListeners(best, getAverageFitness(population), i);

                List<Chromosome> newPopulation = new ArrayList<>();

                newPopulation.add(population.get(0));
                newPopulation.add(population.get(1));

                double steps = (populationSize - newPopulation.size()) / 2.;
                List<Callable<Pair>> callables = new ArrayList<>();
                List<Chromosome> old = population;
                Callable<Pair> callable = () -> {
                    Pair parents = selectParents(old, tournamentSize);
                    Pair children = crossover(parents, alpha);

                    mutate(children.first, sigma);
                    mutate(children.second, sigma);

                    return children;
                };

                for (int j = 0; j < steps; j++) {
                    callables.add(callable);
                }

                try {
                    List<Future<Pair>> newPop = pool.invokeAll(callables);
                    for (Future<Pair> children : newPop) {
                        Pair c = children.get();
                        newPopulation.add(c.first);

                        if (newPopulation.size() >= populationSize) {
                            break;
                        }
                        newPopulation.add(c.second);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                population = newPopulation;
            }

            population = sortPopulation(population);
            Chromosome best = population.get(0);

            simulator.generateGrids(10000, 10, 10, false, ThreadLocalRandom.current());
            evaluateSolution(best);
            System.out.println(best);
        } finally {
            pool.shutdown();
        }
        return null;
    }

    private ElmanNeuralNetwork prepareBest(Chromosome best) {
        ElmanNeuralNetwork robot = network.get();
        robot.setWeights(best.getWeights());
        robot.setStandardizedFitness(best.getFitness());
        return robot;
    }

    private void mutate(Chromosome child, double sigma) {
        double[] chromosome = child.getWeights();

        for (int i = 0; i < chromosome.length; i++) {
            chromosome[i] += ThreadLocalRandom.current().nextGaussian() * sigma;
        }
    }

    private Pair crossover(Pair parents, double alpha) {
        double[] firstParent = parents.first.getWeights();
        double[] secondParent = parents.second.getWeights();

        double[] firstChild = new double[firstParent.length];
        double[] secondChild = new double[secondParent.length];

        for (int i = 0; i < firstChild.length; i++) {
            double min = Math.min(firstParent[i], secondParent[i]);
            double max = Math.max(firstParent[i], secondParent[i]);
            double delta = max - min;

            firstChild[i] = ThreadLocalRandom.current().nextDouble() * delta * (2 * alpha + 1) + min - delta * alpha;
            secondChild[i] = ThreadLocalRandom.current().nextDouble() * delta * (2 * alpha + 1) + min - delta * alpha;
        }

        return new Pair(new Chromosome(firstChild), new Chromosome(secondChild));
    }

    private Pair selectParents(List<Chromosome> population, int tournamentSize) {
        Chromosome first = tournamentSelection(population, tournamentSize);
        Chromosome second;
        do {
            second = tournamentSelection(population, tournamentSize);
        } while (first == second);

        return new Pair(first, second);
    }

    private Chromosome tournamentSelection(
            List<Chromosome> population, int tournamentSize) {
        Set<Chromosome> chosen = new HashSet<>();
        int populationSize = population.size();

        int size = 0;
        while (size < tournamentSize) {
            Chromosome chromosome = population.get(ThreadLocalRandom.current().nextInt(populationSize));
            if (chosen.add(chromosome)) {
                size++;
            }
        }
        List<Chromosome> chosenList = new ArrayList<>(chosen);
        chosenList = sortPopulation(chosenList);

        return chosenList.get(0);
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
            total += (10. * stat.getBottlesCollected() - 1 * stat.getEmptyPickups() - 5 * stat.getWallsHit()) /
                     (10 * (stat.getBottlesCollected() + stat.getBottlesLeft()));
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

    private double getAverageFitness(List<Chromosome> population) {
        return population.stream().mapToDouble(c -> c.getFitness()).average().getAsDouble();
    }

    private static class Pair {
        public Chromosome first;
        public Chromosome second;

        public Pair(Chromosome first, Chromosome second) {
            this.first = first;
            this.second = second;
        }
    }

    @Override
    public String toString() {
        return "Elman neural network";
    }
}
