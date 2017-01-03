package hr.fer.zemris.projekt.algorithms.neural.ffann.ga;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.neural.ActivationFunction;
import hr.fer.zemris.projekt.algorithms.neural.Utils;
import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;
import hr.fer.zemris.projekt.algorithms.neural.ffann.FFANN;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.compFactor.ICompFactor;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.compFactor.LinearCompFactor;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.crossover.ICrossover;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.crossover.TwoPointCrossover;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.mutation.GausMutation;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.mutation.IMutation;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.selection.ISelection;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.selection.RandomSelection;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.selection.TournamentSelection;
import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.Stats;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static hr.fer.zemris.projekt.algorithms.neural.ActivationFunction.HYP_TAN;
import static hr.fer.zemris.projekt.algorithms.neural.ActivationFunction.SIGMOID;
import static hr.fer.zemris.projekt.algorithms.neural.ffann.ga.SASEGASAParameters.*;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class SASEGASA extends ObservableAlgorithm {
    private static final double COLLECTED_VALUE = 10;
    private static final double EMPTY_PICKUP = -1;
    private static final double WALL_HIT = -3;

    private FFANN network;
    private AbstractSimulator simulator;

    private int chromosomeSize;
    private int populationSize;
    private int numberOfVillages;
    private int maxGenerations;
    private int tournamentSize;
    private int maxIteration;
    private double successRatio;
    private double maxSelectionPressure;
    private double mutationRate;

    private ISelection firstSelection;
    private ISelection secondSelection;
    private ICrossover crossover;
    private IMutation mutation;
    private ICompFactor compFactor;

    @Override
    public Robot readSolutionFromFile(Path filePath) throws IOException {
        return null;
    }

    @Override
    public void writeSolutionToFile(Path filePath, Robot robot) throws IOException {

    }

    @Override
    public Parameters<? extends Algorithm> getDefaultParameters() {
        return new SASEGASAParameters();
    }

    @Override
    public Robot run(AbstractSimulator simulator, Parameters<? extends Algorithm> parameters) {
        this.simulator = simulator;
        initialize(parameters);

        List<Chromosome> population = generatePopulation();

        while (numberOfVillages > 0){
            List<Chromosome> newPopulation = new ArrayList<>();

            for (int i = 0; i < numberOfVillages; ++i){
                int begin = i * populationSize / numberOfVillages;
                int end = (i + 1) * populationSize / numberOfVillages;

                if (end > populationSize || i == numberOfVillages - 1){
                    end = populationSize;
                }

                List<Chromosome> currentPopulation = new ArrayList<>();
                for (int j = begin; j < end; ++j){
                    currentPopulation.add(population.get(j));
                }

                currentPopulation = run(currentPopulation);
                newPopulation.addAll(currentPopulation);
            }
            numberOfVillages--;
            population = newPopulation;
            System.out.println(bestOfPopulation(population));
        }
        return null;
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

    private List<Chromosome> run(List<Chromosome> population) {
        int poolSize;
        int generation = 0;
        double selectionPressure = 0;
        int successSize = (int)(population.size() * successRatio);
        if (successSize < 1) successSize = 1;
        int maxPool = population.size() - successSize;

        compFactor.reset();
        while (selectionPressure < maxSelectionPressure && generation < maxGenerations){
            generation++;
            compFactor.nextFactor();
            Set<Chromosome> newPopulation = new HashSet<>();
            Set<Chromosome> pool = new HashSet<>();

            poolSize = 0;
            while (selectionPressure < maxSelectionPressure && newPopulation.size() < successSize){
                Chromosome firstParent = firstSelection.select(population);
                Chromosome secondParent = secondSelection.select(population);
                while (secondParent == firstParent){
                    secondParent = secondSelection.select(population);
                }

                Chromosome child = crossover.crossover(firstParent, secondParent);
                mutation.mutate(child);
                evaluate(child);

                double f1 = firstParent.getFitness();
                double f2 = secondParent.getFitness();
                if (f1 > f2){
                    double tmp = f1;
                    f1 = f2;
                    f2 = tmp;
                }

                double f = child.getFitness();
                if (f >= f1 + compFactor.getFactor() * (f2 - f1)){
                    newPopulation.add(child);
                } else {
                    if (pool.size() < maxPool){
                        pool.add(child);
                    }
                    poolSize++;
                }
                selectionPressure = (double)poolSize / population.size();
            }

            newPopulation.addAll(pool);
            int iteration = 0;
            while (newPopulation.size() < population.size() && iteration < maxIteration){
                iteration++;
                Chromosome firstParent = firstSelection.select(population);
                Chromosome secondParent = secondSelection.select(population);

                Chromosome child = crossover.crossover(firstParent, secondParent);
                mutation.mutate(child);
                evaluate(child);
                newPopulation.add(child);
            }

            if (newPopulation.size() < population.size()){
                int expectedSize = population.size();
                population = new ArrayList<>(newPopulation);

                while (population.size() < expectedSize){
                    population.add(population.get(Utils.RANDOM.nextInt(population.size())));
                }
                break;
            }

            population = new ArrayList<>(newPopulation);
            System.out.println(bestOfPopulation(population));
        }

        System.out.println("Village Completed!");
        return population;
    }

    private List<Chromosome> generatePopulation() {
        List<Chromosome> population = new ArrayList<>();

        for (int i = 0; i < populationSize; ++i){
            Chromosome newChromosome = new Chromosome(chromosomeSize);
            evaluate(newChromosome);
            population.add(newChromosome);
        }

        return population;
    }

    private void evaluate(Chromosome chromosome) {
        network.setWeights(chromosome.getWeights());

        List<Stats> stats = simulator.playGames(network);

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
    }

    private void initialize(Parameters<? extends Algorithm> parameters) {
        if (!(parameters instanceof SASEGASAParameters)){
            throw new IllegalStateException("The given parameters are invalid! Expected SASEGASA parameters!");
        }

        network = new FFANN(
                new int[]{15, 15, 7},
                new ActivationFunction[] {HYP_TAN, SIGMOID, HYP_TAN}
        );

        chromosomeSize = network.getNumberOfWeights();
        populationSize = (int) parameters.getParameter(POPULATION_SIZE).getValue();
        numberOfVillages = (int) parameters.getParameter(NUMBER_OF_VILLAGES).getValue();
        maxGenerations = (int) parameters.getParameter(MAX_GENERATIONS).getValue();
        tournamentSize = (int) parameters.getParameter(TOURNAMENT_SIZE).getValue();
        successRatio = parameters.getParameter(SUCCESS_RATIO).getValue();
        maxSelectionPressure = parameters.getParameter(MAX_SELECTION_PRESSURE).getValue();
        mutationRate = parameters.getParameter(MUTATION_RATE).getValue();
        maxIteration = (int) parameters.getParameter(MAX_ITERATION).getValue();

        firstSelection = new TournamentSelection(tournamentSize);
        secondSelection = new RandomSelection();
        crossover = new TwoPointCrossover();
        mutation = new GausMutation(mutationRate);
        compFactor = new LinearCompFactor(maxGenerations);
    }
}
