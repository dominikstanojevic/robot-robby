package hr.fer.zemris.projekt.algorithms.neural;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dominik on 5.12.2016..
 */
public class ContextLayer {
    private RealVector values;
    private RealVector weights;

    public ContextLayer(int size) {
        this.values = new ArrayRealVector(size);
        this.weights = Utils.createRandomVector(size, -1, 1);
    }

    void copyValues(RealVector values) {
        this.values = new ArrayRealVector(values);
    }

    RealVector getValues() {
        return Utils.ebeMultiply(values, weights);
    }
}
