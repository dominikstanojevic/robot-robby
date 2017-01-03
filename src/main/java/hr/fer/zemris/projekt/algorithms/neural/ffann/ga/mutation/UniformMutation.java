package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.mutation;

import hr.fer.zemris.projekt.algorithms.neural.Utils;

import java.util.function.Function;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class UniformMutation extends RandomMutation {
    private static Function<Double, Double> change = x -> (Utils.RANDOM.nextDouble() - 0.5) * 2 * x;

    public UniformMutation(double mutationRate, double sigma) {
        super(mutationRate, sigma, change);
    }
}
