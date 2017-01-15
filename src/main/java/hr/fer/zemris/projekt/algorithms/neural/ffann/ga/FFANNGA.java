package hr.fer.zemris.projekt.algorithms.neural.ffann.ga;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.neural.ActivationFunction;
import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;
import hr.fer.zemris.projekt.algorithms.neural.ffann.FFANN;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.crossover.ICrossover;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.crossover.IntervalCrossover;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.mutation.GausMutation;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.mutation.IMutation;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.selection.ISelection;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.selection.TournamentSelection;
import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.Stats;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static hr.fer.zemris.projekt.algorithms.neural.ActivationFunction.HYP_TAN;
import static hr.fer.zemris.projekt.algorithms.neural.ActivationFunction.SIGMOID;
import static hr.fer.zemris.projekt.algorithms.neural.ffann.ga.FFANGAParameters.*;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class FFANNGA extends ObservableAlgorithm {
    private static final double COLLECTED_VALUE = 10;
    private static final double EMPTY_PICKUP = -1;
    private static final double WALL_HIT = -3;

    private ThreadLocal<FFANN> network;
    private AbstractSimulator simulator;

    private int chromosomeSize;
    private int populationSize;
    private int maxGenerations;
    private int tournamentSize;
    private double mutationRate;
    private double sigma;
    private double alpha;

    private double startingIntervalMin;
    private double startingIntervalMax;

    private ISelection firstSelection;
    private ISelection secondSelection;
    private ICrossover crossover;
    private IMutation mutation;

    //TODO
    @Override
    public Robot readSolutionFromFile(Path filePath) throws IOException {
        return null;
    }

    //TODO
    @Override
    public void writeSolutionToFile(Path filePath, Robot robot) throws IOException {

    }

    @Override
    public Parameters<? extends Algorithm> getDefaultParameters() {
        return new FFANGAParameters();
    }

    @Override
    public Robot run(AbstractSimulator simulator, Parameters<? extends Algorithm> parameters) {
        this.simulator = simulator;
        initialize(parameters);
        ExecutorService pool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors(),
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                });

        List<Chromosome> population = generatePopulation();
        evaluate(population, pool);

        int generation = 0;
        while (generation < maxGenerations){
            generation++;
            if (generation % 100 == 0){
                simulator.generateGrids(50, 10, 10, false, ThreadLocalRandom.current());
            }

            List<Chromosome> newPopulation = new ArrayList<>();
            newPopulation.add(bestOfPopulation(population));

            while (newPopulation.size() < populationSize){
                Chromosome firstParent = firstSelection.select(population);
                Chromosome secondParent = secondSelection.select(population);
                while (secondParent.equals(firstParent)){
                    secondParent = secondSelection.select(population);
                }

                Chromosome child = crossover.crossover(firstParent, secondParent);
                mutation.mutate(child);

                newPopulation.add(child);
            }

            evaluate(newPopulation, pool);
            population = newPopulation;

            Chromosome bestChromosome = bestOfPopulation(population);
            network.get().setStandardizedFitness(bestChromosome.getFitness());

            FFANN ffann = network.get().copy();
            ffann.setWeights(bestChromosome.getWeights());
            ffann.setStandardizedFitness(bestChromosome.getFitness());
            this.notifyListeners(ffann, populationAverage(population), generation);
        }

        pool.shutdown();
        Chromosome bestChromosome = bestOfPopulation(population);
        network.get().setWeights(bestChromosome.getWeights());
        return network.get();
    }

    private Chromosome bestOfPopulation(List<Chromosome> population) {
        Chromosome best = population.get(0);
        double bestFitness = best.getFitness();

        for (Chromosome chromosome : population){
            if (chromosome.getFitness() > bestFitness){
                bestFitness = chromosome.getFitness();
                best = chromosome;
            }
        }

        return best;
    }

    private double populationAverage(List<Chromosome> population) {
        double fitnessSum = 0;

        for (Chromosome chromosome : population){
            fitnessSum += chromosome.getFitness();
        }

        return fitnessSum / population.size();
    }

    private List<Chromosome> generatePopulation() {
        List<Chromosome> population = new ArrayList<>();

        for (int i = 0; i < populationSize; ++i){
            Chromosome newChromosome = new Chromosome(chromosomeSize, startingIntervalMin, startingIntervalMax);
            population.add(newChromosome);
        }

        return population;
    }

    private void evaluate(List<Chromosome> population, ExecutorService pool) {
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

    private Void evaluateSolution(Chromosome chromosome) {
        network.get().setWeights(chromosome.getWeights());

        List<Stats> stats = simulator.playGames(network.get());

        double fitness = 0;
        for (Stats stat : stats) {
            double tmp = stat.getBottlesCollected() * COLLECTED_VALUE;
            tmp += stat.getEmptyPickups() * EMPTY_PICKUP;
            tmp += stat.getWallsHit() * WALL_HIT;

            tmp /= (COLLECTED_VALUE * (stat.getBottlesCollected() + stat.getBottlesLeft()));
            fitness += tmp;
        }
        fitness /= stats.size();

        chromosome.setFitness(fitness);
        return null;
    }

    private void initialize(Parameters<? extends Algorithm> parameters) {
        if (!(parameters instanceof FFANGAParameters)){
            throw new IllegalStateException("The given parameters are invalid! Expected FFANNGA parameters!");
        }

        network = ThreadLocal.withInitial(() -> new FFANN(
                new int[]{15, 7, 7},
                new ActivationFunction[] {SIGMOID, HYP_TAN, HYP_TAN}
        ));

        chromosomeSize = network.get().getNumberOfWeights();
        populationSize = (int) parameters.getParameter(POPULATION_SIZE).getValue();
        maxGenerations = (int) parameters.getParameter(MAX_GENERATIONS).getValue();
        tournamentSize = (int) parameters.getParameter(TOURNAMENT_SIZE).getValue();
        mutationRate = parameters.getParameter(MUTATION_RATE).getValue();
        sigma = parameters.getParameter(SIGMA).getValue();
        startingIntervalMin = parameters.getParameter(STARTING_INTERVAL_MIN).getValue();
        startingIntervalMax = parameters.getParameter(STARTING_INTERVAL_MAX).getValue();
        alpha = parameters.getParameter(ALPHA).getValue();

        firstSelection = new TournamentSelection(tournamentSize);
        secondSelection = new TournamentSelection(tournamentSize);
        crossover = new IntervalCrossover(alpha);
        mutation = new GausMutation(mutationRate, sigma);
    }

    @Override
    public String toString() {
        return "Feedforward neural network";
    }
}
