package hr.fer.zemris.projekt.algorithms.neural.elman.ga;

import hr.fer.zemris.projekt.algorithms.neural.Utils;

/**
 * @author Dominik Stanojevic, Kristijan Vulinovic
 * @version 1.1.0
 */
public class Chromosome implements Comparable<Chromosome>{
    private double[] weights;
    private double fitness;

    public Chromosome(int size) {
        weights = new double[size];

        for (int i = 0; i < size; i++) {
            weights[i] = Utils.RANDOM.nextDouble();
        }
    }

    public Chromosome(double[] chromosome) {
        weights = chromosome;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
       return "Fitness: " + fitness;
    }

    @Override
    public int compareTo(Chromosome o) {
        return Double.compare(this.getFitness(), o.getFitness());
    }
}
