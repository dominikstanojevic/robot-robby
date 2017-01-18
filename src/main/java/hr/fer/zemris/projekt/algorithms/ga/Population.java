package hr.fer.zemris.projekt.algorithms.ga;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Represents a population of {@link Chromosome chromosomes}. Provides
 * methods required for generating a randomized population, retrieving
 * the best individual and evolving the population into the next generation.
 */
public class Population implements Iterable<Chromosome> {

    /** {@link Random} object. */
    private static final Random RANDOM = new Random();

    /** Minimal population size. */
    public static final int MIN_POPULATION_SIZE = 2;

    /**
     * Size of the population.
     */
    private int populationSize;

    /**
     * {@link Chromosome Individuals} in the population.
     */
    private List<Chromosome> chromosomes;

    /**
     * Constructs a {@link Population} with the specified size.
     * @param populationSize size of the population, cannot be less than 2
     */
    private Population(int populationSize) {
        if (populationSize < MIN_POPULATION_SIZE) {
            throw new IllegalArgumentException(
                    "Population size cannot be less than " +
                            MIN_POPULATION_SIZE +
                            ": " + populationSize);
        }

        this.populationSize = populationSize;
        this.chromosomes = new ArrayList<>(populationSize);
    }

    /**
     * Generates a starting population of randomized {@link Chromosome individuals}.
     *
     * @param populationSize size of the population, cannot be less than 2
     * @return starting population.
     */
    public static Population generatePopulation(int populationSize) {

        Population population = new Population(populationSize);

        for (int i = 0; i < populationSize; i++) {
            population.chromosomes.add(Chromosome.generateRandom(RANDOM));
        }

        return population;
    }

    /**
     * Returns the best {@link Chromosome} in the current population.
     * @return the best {@link Chromosome} in the current population.
     */
    public Chromosome getBest() {
        return chromosomes.get(0);
    }

    /**
     * Sorts the members of the population based on fitness.
     */
    public void sort() {
        chromosomes.sort(Chromosome.BY_FITNESS.reversed());
    }

    /**
     * Returns a random {@link Chromosome} from the current population.
     * @param random {@link Random} object
     * @return a random {@code Chromosome} from the population
     */
    public Chromosome pickRandom(Random random) {
        return chromosomes.get(random.nextInt(chromosomes.size()));
    }

    /**
     * Evolves the population into the next generation.
     *  @param elitismRatio   elitism ratio, determines the percentage of best results from the
     *                       previous generation which will remain in the next generation
     * @param tournamentSize size of the tournament used to pick the parents
     */
    public void evolve(double elitismRatio, int tournamentSize) {
        if (elitismRatio < 0 || elitismRatio > 1) {
            throw new IllegalArgumentException("Elitism ratio must be between 0 and 1, not " + elitismRatio);
        }
        if (tournamentSize < 1) {
            throw new IllegalArgumentException("Tournament size must be at least 1, not " + tournamentSize);
        }

        sort();

        List<Chromosome> newChromosomes = new ArrayList<>(populationSize);
        newChromosomes.addAll(chromosomes.subList(0, (int) (populationSize * elitismRatio)));

        while (newChromosomes.size() < populationSize) {

            Chromosome[] parents = pickParents(tournamentSize);
            Chromosome[] children = parents[0].crossover(parents[1], RANDOM);

            for (Chromosome child : children) {
                if (newChromosomes.size() >= populationSize) {
                    break;
                }

                child.mutate(RANDOM);
                newChromosomes.add(child);
            }
        }

        chromosomes = newChromosomes;
    }

    /**
     * Chooses two parents, where each is picked with a n-tournament selection.
     * @param tournamentSize the size of the tournament
     * @return the two parents
     */
    private Chromosome[] pickParents(int tournamentSize) {
        Chromosome first = tournamentSelect(tournamentSize);
        Chromosome second = null;

        while (second == null || first == second) {
            second = tournamentSelect(tournamentSize);
        }

        return new Chromosome[] { first, second };
    }

    /**
     * Performs a tournament selection on the current population.
     * @param tournamentSize size of the tournament
     * @return winner of the tournament
     */
    private Chromosome tournamentSelect(int tournamentSize) {
        Chromosome candidate = pickRandom(RANDOM);

        for (int i = 1; i < tournamentSize; i++) {
            Chromosome competitor = pickRandom(RANDOM);

            if (competitor.getFitness() > candidate.getFitness()) {
                candidate = competitor;
            }
        }

        return candidate;
    }

    public double calculateAvgFitness() {
        return chromosomes.stream()
                .mapToDouble(Chromosome::getFitness)
                .average().getAsDouble();
    }

    @Override
    public Iterator<Chromosome> iterator() {
        return chromosomes.iterator();
    }
}
