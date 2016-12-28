package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.mutation;

import hr.fer.zemris.projekt.algorithms.neural.Utils;
import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;

import java.util.function.Function;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public abstract class RandomMutation implements IMutation {
    private double mutationRate;
    private double sigma;
    private Function<Double, Double> change;

    public RandomMutation(double mutationRate, double sigma, Function<Double, Double> change) {
        this.mutationRate = mutationRate;
        this.sigma = sigma;
        this.change = change;
    }

    @Override
    public void mutate(Chromosome chromosome) {
        double[] weights = chromosome.getWeights();

        for (int i = 0; i < weights.length; ++i){
            if (Utils.RANDOM.nextDouble() < mutationRate){
                weights[i] += change.apply(sigma);
            }
        }

        chromosome.setWeights(weights);
    }
}
