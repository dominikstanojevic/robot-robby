package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.selection;

import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;

import java.util.List;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public interface ISelection {
    Chromosome select(List<Chromosome> population);
}
