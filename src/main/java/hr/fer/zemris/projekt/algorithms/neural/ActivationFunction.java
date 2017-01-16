package hr.fer.zemris.projekt.algorithms.neural;

import java.util.function.Function;

/**
 * <p>Activation functions for neural networks. Available activation functions:</p>
 * <ul>
 * <li>Step function</li>
 * <li>Linear function</li>
 * <li>Sigmoid function</li>
 * <li>Hyperbolic tangent function</li>
 * </ul>
 *
 * @author Dominik Stanojevic
 * @version 1.0
 */
public enum ActivationFunction {
    /**
     * Step function
     */
    STEP(x -> x < 0 ? 0. : 1.),

    /**
     * Linear function
     */
    LINEAR(x -> x),

    /**
     * Sigmoid function
     */
    SIGMOID(x -> 1 / (1 + Math.pow(Math.E, -x))),

    /**
     * Hyperbolic tangent function
     */
    HYP_TAN(Math::tanh);

    /**
     * Constructs new activation function.
     *
     * @param function representing newly created activation function
     */
    ActivationFunction(Function<Double, Double> function) {
        this.function = function;
    }

    /**
     * function
     */
    private Function<Double, Double> function;

    /**
     * Calculates function value at a given point.
     *
     * @param x variable
     * @return value for given point
     */
    public double valueAt(double x) {
        return function.apply(x);
    }
}
