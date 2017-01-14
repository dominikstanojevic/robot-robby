package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.compFactor;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class LinearCompFactor implements ICompFactor {
    private int maxIterations;
    private int currentIteration;
    private double factor;

    public LinearCompFactor(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override
    public void reset() {
        currentIteration = 0;
        factor = 0;
    }

    @Override
    public void nextFactor() {
        currentIteration++;
        factor = ((double) currentIteration) / maxIterations;
    }

    @Override
    public double getFactor() {
        return factor;
    }
}
