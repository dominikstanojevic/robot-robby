package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.crossover;

import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public interface ICrossover {
    Chromosome crossover(Chromosome parent1, Chromosome parent2);
}
