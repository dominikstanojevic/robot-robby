package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.compFactor;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public interface ICompFactor {
    void reset();
    void nextFactor();
    double getFactor();
}
