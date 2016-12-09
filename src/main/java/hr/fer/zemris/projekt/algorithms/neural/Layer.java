package hr.fer.zemris.projekt.algorithms.neural;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Dominik on 5.12.2016..
 */
public class Layer {
    private RealVector values;
    private ActivationFunction activationFunction;

    private Layer previous;
    private Layer next;
    private ContextLayer context;

    private RealMatrix weights;

    public Layer(int size, ActivationFunction function) {
        if (size < 1) {
            throw new NeuralNetworkException("Layer should have at lease one neuron.");
        }
        Objects.requireNonNull(function, "Activation function cannot be null.");

        values = new ArrayRealVector(size);
        this.activationFunction = function;
    }

    public Layer(int size) {
        this(size, ActivationFunction.SIGMOID);
    }

    public int numberOfNeurons() {
        return values.getDimension();
    }

    public void giveInput(RealVector input) {
        if (previous != null) {
            throw new NeuralNetworkException("Cannot give input to non-input layer.");
        }

        Objects.requireNonNull(input, "Input cannot be null.");
        if (input.getDimension() != values.getDimension()) {
            throw new NeuralNetworkException("Given input has different size than input layer.");
        }

        values = input;
    }

    public RealVector calculateOutput() {
        RealVector result;
        if (previous == null) {
            result = values;
        } else {
            result = Utils.map(values, activationFunction);
        }

        if (context != null) {
            context.copyValues(result);
        }

        if (next != null) {
            fire(result);
        }
        return result;
    }

    private void fire(RealVector result) {
        Objects.requireNonNull(result, "Result vector cannot be null.");

        result = new ArrayRealVector(result, new ArrayRealVector(new double[] { 1 }));

        for (int i = 0, n = next.numberOfNeurons(); i < n; i++) {
            RealVector weightsForNeuron = weights.getColumnVector(i);

            double total = result.dotProduct(weightsForNeuron);
            next.setValue(i, total);
        }
    }

    private void setValue(int position, double value) {
        values.setEntry(position, value);
    }

    public void activateContextLayer() {
        if (context == null) {
            context = new ContextLayer(values.getDimension());
        } else {
            throw new NeuralNetworkException("Context layer is already activated.");
        }
    }

    public void setNext(Layer layer, RealMatrix weights) {
        Objects.requireNonNull(layer, "Layer cannot be null.");
        Objects.requireNonNull(weights, "Weights matrix cannot be null.");

        this.next = layer;
        this.weights = weights;

        layer.setPrevious(this);
    }

    public void setNext(Layer layer) {
        Objects.requireNonNull(layer, "Layer cannot be null.");

        //number of neurons + one weight for bias neuron
        setNext(layer, Utils.createRandomMatrix(values.getDimension() + 1, layer.numberOfNeurons(), -1, 1));
    }

    public void setPrevious(Layer layer) {
        Objects.requireNonNull(layer, "Layer cannot be null.");

        this.previous = layer;
    }

    public void deactivateContextLayer() {
        if (context == null) {
            throw new NeuralNetworkException("Context layer is not activated");
        } else {
            context = null;
        }
    }

    public boolean isInput() {
        return previous == null;
    }

    public boolean isOutput() {
        return next == null;
    }

    public void setWeightMatrix(RealMatrix weights) {
        this.weights = weights;
    }

    public void setWeightMatrix(double[] weights) {
        int m = this.weights.getRowDimension();
        int n = this.weights.getColumnDimension();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                this.weights.setEntry(i, j, weights[i * n + j]);
            }
        }

        if (context != null) {
            context.setWeights(Arrays.copyOfRange(weights, m * n, weights.length));
        }
    }

    public int numberOfWeights() {
        if (weights == null) {
            return 0;
        }

        int total = weights.getColumnDimension() * weights.getRowDimension();
        if (context != null) {
            total += context.numberOfWeights();
        }

        return total;
    }

    public void clearContext() {
        if (context != null) {
            context.clear();
        }
    }

    public double[] getWeights() {
        double[] weights = new double[numberOfWeights()];

        int m = this.weights.getRowDimension();
        int n = this.weights.getColumnDimension();

        for (int i = 0; i < m; i++) {
            RealVector row = this.weights.getRowVector(i);
            System.arraycopy(row, 0, weights, i * n, n);
        }

        if (context != null) {
            double[] contextWeights = context.getWeights().toArray();
            System.arraycopy(contextWeights, 0, weights, m * n, contextWeights.length);
        }

        return weights;
    }
}
