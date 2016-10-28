package hr.fer.zemris.projekt.parameter;

import hr.fer.zemris.projekt.algorithms.Algorithm;

import java.util.Set;

/**
 * Represents the parameters for an algorithm. Objects implementing
 * this interface are in charge of keeping track of the parameters
 * for a given algorithm, and implemented the functionality of
 * retrieving and setting a {@link Parameter} by it's name.
 *
 * @param <T> {@link Algorithm} implementation
 * @author Leon Luttenberger
 */
public interface Parameters<T extends Algorithm> {

    /**
     * Returns the {@link Parameter} represented by the specified name.
     *
     * @param name name representing the {@code Parameter}
     * @return the {@link Parameter} represented by the specified name
     */
    Parameter getParameter(String name);

    /**
     * Sets the value of the {@link Parameter} represented by the specified
     * name.
     *
     * @param name  name representing the {@code Parameter}
     * @param value value to set
     */
    void setParameter(String name, double value);

    /**
     * Returns a set of {@link Parameter} objects.
     *
     * @return a set of {@link Parameter} objects
     */
    Set<Parameter> getParameters();
}
