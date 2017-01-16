package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.crossover;

import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class TwoPointCrossover implements ICrossover {
    @Override
    public Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        double[] weights1 = parent1.getWeights();
        double[] weights2 = parent2.getWeights();

        int n = weights1.length;
        int firstPoint = ThreadLocalRandom.current().nextInt(n);
        int secontPoint = ThreadLocalRandom.current().nextInt(n);

        if (firstPoint > secontPoint){
            int tmp = firstPoint;
            firstPoint = secontPoint;
            secontPoint = tmp;
        }

        double[] weights = new double[n];
        System.arraycopy(weights1, 0, weights, 0, firstPoint);
        System.arraycopy(weights2, firstPoint, weights, firstPoint, secontPoint - firstPoint);
        System.arraycopy(weights1, secontPoint, weights, secontPoint, n - secontPoint);

        return new Chromosome(weights);
    }
}
