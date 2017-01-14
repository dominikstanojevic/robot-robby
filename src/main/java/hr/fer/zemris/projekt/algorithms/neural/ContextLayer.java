package hr.fer.zemris.projekt.algorithms.neural;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dominik on 5.12.2016..
 */
public class ContextLayer {
    private RealVector values;
    private RealMatrix weights;

    public ContextLayer(int size) {
        this.values = new ArrayRealVector(size);
        this.weights = MatrixUtils.createRealMatrix(new double[size][size]);
    }

    void copyValues(RealVector values) {
        this.values = new ArrayRealVector(values);
    }

    RealVector getValues() {
        int n = values.getDimension();
        double[] result = new double[n];

        for (int i = 0; i < n; i++) {
            RealVector weightsForNeuron = weights.getColumnVector(i);

            result[i] = values.dotProduct(weightsForNeuron);
        }

        return new ArrayRealVector(result);
    }

    public void clear() {
        values = new ArrayRealVector(new double[values.getDimension()]);
    }

    public int numberOfWeights() {
        int n = values.getDimension();
        return n * n;
    }

    public void setWeights(double[] weights) {
        int n = values.getDimension();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                this.weights.setEntry(i, j, weights[i * n + j]);
            }
        }
    }

    public RealVector getWeights() {
        int n = values.getDimension();
        double[] weights = new double[n * n];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                weights[i * n + j] = this.weights.getEntry(i, j);
            }
        }

        return new ArrayRealVector(weights);
    }
}
