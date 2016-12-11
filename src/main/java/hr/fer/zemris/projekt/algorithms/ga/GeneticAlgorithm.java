package hr.fer.zemris.projekt.algorithms.ga;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.Stats;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>A generational genetic algorithm which is used to trained Robby the Robot,
 * which uses elitism, single-point crossovers along with guaranteed mutations
 * for each child.</p>
 *
 * <p>This class is also an {@link ObservableAlgorithm}, which allow it's generational
 * results to be accessible by the caller.</p>
 *
 * @author Leon Luttenberger
 */
public class GeneticAlgorithm extends ObservableAlgorithm {

    /**
     * The default {@link GAParameters parameters} for this algorithm.
     */
    private static final GAParameters DEFAULT_PARAMS = new GAParameters();

    @Override
    public Robot readSolutionFromFile(Path filePath) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Robot) ois.readObject();
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeSolutionToFile(Path filePath, Robot robot) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(robot);
        }
    }

    @Override
    public Parameters<? extends Algorithm> getDefaultParameters() {
        return DEFAULT_PARAMS.copy();
    }

    @Override
    public Robot run(AbstractSimulator simulator, Parameters<? extends Algorithm> parameters) {
        if (!(parameters instanceof GAParameters)) {
            throw new IllegalArgumentException("Wrong parameters instance.");
        }

        GAParameters gaParameters = (GAParameters) parameters;
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // load parameters
        int populationSize = (int) gaParameters.populationSize.getValue();
        int maxGenerations = (int) gaParameters.maxGenerations.getValue();
        double elitismRatio = gaParameters.elitismRatio.getValue();
        int tournamentSize = (int) gaParameters.tournamentSize.getValue();

        Population population = Population.generatePopulation(populationSize);
        evaluatePopulation(simulator, population, pool);

        for (int i = 1; i <= maxGenerations; i++) {
            population.evolve(elitismRatio, tournamentSize);
            evaluatePopulation(simulator, population, pool);

            Chromosome best = population.getBest();

            this.notifyListeners(best.getFitness());
        }

        pool.shutdown();
        return population.getBest();
    }

    /**
     * Iterates through the population and calculates the fitness for each individual. After the evaluation
     * is done, the list is then sorted.
     *
     * @param simulator simulator for evaluating the individuals
     * @param population population to evaluate
     */
    private void evaluatePopulation(AbstractSimulator simulator, Population population, ExecutorService pool) {
        List<Future<List<Stats>>> results = new ArrayList<>();
        for (Chromosome chromosome : population) {
            Callable<List<Stats>> job = () -> simulator.playGames(chromosome);
            results.add(pool.submit(job));
        }

        Iterator<Future<List<Stats>>> resultsIter = results.iterator();
        Iterator<Chromosome> chromosomeIter = population.iterator();

        while (chromosomeIter.hasNext()) {
            Future<List<Stats>> job = resultsIter.next();
            Chromosome chromosome = chromosomeIter.next();

            try {
                List<Stats> stats = job.get();
                chromosome.setFitness(calculateFitness(stats));

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        population.sort();
    }

    /**
     * Calculates the fitness of an individual based on the {@link Stats} for each
     * cleaning sessions.
     * @param stats list of {@link Stats} for each cleaning session
     * @return fitness of the individual in question
     */
    public static double calculateFitness(List<Stats> stats) {
        double fitness = 0;

        for (Stats stat : stats) {
            double max = (stat.getBottlesCollected() + stat.getBottlesLeft()) * 2;

            fitness += stat.getBottlesCollected() * 2 / max;
            fitness -= stat.getEmptyPickups() / max;
            fitness -= stat.getWallsHit() / max;
        }

        return fitness / stats.size();
    }
}
