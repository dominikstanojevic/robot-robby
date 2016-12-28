package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.mutation;

import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public interface IMutation {
    void mutate(Chromosome chromosome);
}
