package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.crossover;

import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class IntervalCrossover implements ICrossover {
    private double alpha;

    public IntervalCrossover(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        double[] weights1 = parent1.getWeights();
        double[] weights2 = parent2.getWeights();

        int n = weights1.length;
        double[] weights = new double[n];

        for (int i = 0; i < n; i++) {
            double min = Math.min(weights1[i], weights2[i]);
            double max = Math.max(weights1[i], weights2[i]);
            double delta = max - min;

            weights[i] = ThreadLocalRandom.current().nextDouble() * delta * (2 * alpha + 1) + min - delta * alpha;
        }

        return new Chromosome(weights);
    }
}
