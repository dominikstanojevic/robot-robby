package hr.fer.zemris.projekt.algorithms.ga;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Population implements Iterable<Chromosome> {

    /**
     * {@link Random} object.
     */
    private static final Random RANDOM = new Random();

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
     * @param populationSize size of the population.
     */
    public Population(int populationSize) {
        this.populationSize = populationSize;
        this.chromosomes = new ArrayList<>(populationSize);
    }

    /**
     * Generates a starting population of randomized {@link Chromosome individuals}.
     *
     * @param populationSize size of the population
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

    public void evolve(double elitismRatio, int tournamentSize) {
        sort();

        List<Chromosome> newChromosomes = new ArrayList<>(populationSize);
        newChromosomes.addAll(chromosomes.subList(0, (int) (populationSize * elitismRatio)));

        while (newChromosomes.size() < populationSize) {

            Chromosome[] parents = pickParents(tournamentSize);
            Chromosome[] children = parents[0].mate(parents[1], RANDOM);

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

    private Chromosome[] pickParents(int tournamentSize) {
        Chromosome first = tournamentSelect(tournamentSize);
        Chromosome second = null;

        while (second == null || first == second) {
            second = tournamentSelect(tournamentSize);
        }

        return new Chromosome[] { first, second };
    }

    private Chromosome tournamentSelect(int tournamentSize) {
        Chromosome candidate = chromosomes.get(RANDOM.nextInt(chromosomes.size()));

        for (int i = 1; i < tournamentSize; i++) {
            Chromosome competitor = chromosomes.get(RANDOM.nextInt(chromosomes.size()));

            if (competitor.getFitness() > candidate.getFitness()) {
                candidate = competitor;
            }
        }

        return candidate;
    }

    @Override
    public Iterator<Chromosome> iterator() {
        return chromosomes.iterator();
    }
}
