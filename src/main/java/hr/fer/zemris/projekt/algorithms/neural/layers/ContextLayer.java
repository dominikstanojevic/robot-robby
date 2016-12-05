package hr.fer.zemris.projekt.algorithms.neural.layers;

import hr.fer.zemris.projekt.algorithms.neural.Utils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dominik on 3.12.2016..
 */
public class ContextLayer {
    private RealVector values;

    private RealVector weights;

    public ContextLayer(int size, RealVector weights) {
        this.values = new ArrayRealVector(size);
        this.weights = weights;
    }

    public ContextLayer(int size) {
        this(size, Utils.createRandomVector(size, -1, 1));
    }

    public void copyValues(RealVector values) {
        this.values = new ArrayRealVector(values);
    }

    public RealVector feedValues() {
        return values;
    }
}
