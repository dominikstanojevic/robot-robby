package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.crossover;

import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class OnePointCrossover implements ICrossover {
    @Override
    public Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        double[] weights1 = parent1.getWeights();
        double[] weights2 = parent2.getWeights();

        int n = weights1.length;
        int breakPoint = ThreadLocalRandom.current().nextInt(n);

        double[] weights = new double[n];
        System.arraycopy(weights1, 0, weights, 0, breakPoint);
        System.arraycopy(weights2, breakPoint, weights, breakPoint, n - breakPoint);

        return new Chromosome(weights);
    }
}
