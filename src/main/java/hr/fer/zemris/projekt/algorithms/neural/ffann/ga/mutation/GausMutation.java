package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.mutation;

import hr.fer.zemris.projekt.algorithms.neural.Utils;

import java.util.function.Function;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class GausMutation extends RandomMutation {
    private static Function<Double, Double> change = x -> Utils.RANDOM.nextGaussian() * x;

    public GausMutation(double sigma) {
        super(sigma, change);
    }
}
