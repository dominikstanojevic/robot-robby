package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.mutation;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class GausMutation extends RandomMutation {
    private static Function<Double, Double> change = x -> ThreadLocalRandom.current().nextGaussian() * x;

    public GausMutation(double mutationRate, double sigma) {
        super(mutationRate, sigma, change);
    }
}
