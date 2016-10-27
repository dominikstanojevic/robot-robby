package hr.fer.zemris.projekt.observer.observations;

/**
 * Represents a training result of an algorithm after an iteration. For example, in case of a genetic algorithm,
 * this might represent the fitness of the best individual in a generation.
 *
 * @author Leon Luttenberger
 */
public class TrainingResult {

    /**
     * Value of the training result.
     */
    private double value;

    /**
     * Constructs a {@link TrainingResult} object with the specified value.
     *
     * @param value value of the training result
     */
    public TrainingResult(double value) {
        this.value = value;
    }

    /**
     * Returns the value of the training result.
     *
     * @return the value of the training result
     */
    public double getValue() {
        return value;
    }
}
