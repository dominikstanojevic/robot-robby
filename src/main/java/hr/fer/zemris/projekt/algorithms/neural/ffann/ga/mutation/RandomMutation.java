package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.mutation;

import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;

import java.util.function.Function;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public abstract class RandomMutation implements IMutation {
    private double mutationRate;
    private Function<Double, Double> change;

    public RandomMutation(double mutationRate, Function<Double, Double> change) {
        this.mutationRate = mutationRate;
        this.change = change;
    }

    @Override
    public void mutate(Chromosome chromosome) {
        double[] weights = chromosome.getWeights();

        for (int i = 0; i < weights.length; ++i){
            weights[i] += change.apply(mutationRate);
        }

        chromosome.setWeights(weights);
    }
}
