package hr.fer.zemris.projekt.algorithms.neural.elman.ga;

import hr.fer.zemris.projekt.algorithms.neural.Utils;

import java.util.Arrays;

/**
 * @author Dominik Stanojevic, Kristijan Vulinovic
 * @version 1.1.0
 */
public class Chromosome implements Comparable<Chromosome>{
    private double[] weights;
    private double fitness;
    private int hashCode;
    private boolean hasHashCode;

    public Chromosome(int size) {
        this(size, -0.5, 0.5);
    }

    public Chromosome(int size, double lowerBound, double upperBound) {
        weights = new double[size];

        for (int i = 0; i < size; i++) {
            weights[i] = Utils.RANDOM.nextDouble() * (upperBound - lowerBound) + lowerBound;
        }

        hasHashCode = false;
    }

    public Chromosome(double[] chromosome) {
        weights = chromosome;

        hasHashCode = false;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;

        hasHashCode = false;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chromosome that = (Chromosome) o;

        if (this.hashCode() != that.hashCode()){
            return false;
        }

        return Arrays.equals(weights, that.weights);

    }

    @Override
    public int hashCode() {
        if (hasHashCode) {
            return hashCode;
        }

        hasHashCode = true;
        hashCode = Arrays.hashCode(weights);
        return hashCode;
    }
}
