package hr.fer.zemris.projekt.observer.observations;

import hr.fer.zemris.projekt.algorithms.Robot;

/**
 * Represents a training result of an algorithm after an iteration. For example, in case of a genetic algorithm,
 * this might represent the fitness of the best individual in a generation.
 *
 * @author Leon Luttenberger
 * @version 1.0.0
 */
public class TrainingResult {

    /**
     * Best result of the iteration.
     */
    private Robot bestResult;

    /**
     * Average result of the iteration.
     */
    private Robot averageResult;

    /**
     * Iteration that the algorithm is currently in.
     */
    private int iteration;

    /**
     * Constructs a {@link TrainingResult} object with the specified values.
     *
     * @param bestResult best result of the iteration
     * @param averageResult average result of the iteration
     * @param iteration current iteration
     */
    public TrainingResult(Robot bestResult, Robot averageResult, int iteration) {
        this.bestResult = bestResult;
        this.averageResult = averageResult;
        this.iteration = iteration;
    }

    /**
     * Returns the best result of the iteration.
     *
     * @return the best result of the iteration
     */
    public Robot getBestResult() {
        return bestResult;
    }

    /**
     * Returns the average result of the iteration.
     *
     * @return the average result of the iteration
     */
    public Robot getAverageResult() {
        return averageResult;
    }

    /**
     * Returns the current iteration.
     *
     * @return the current iteration
     */
    public int getIteration() {
        return iteration;
    }
}