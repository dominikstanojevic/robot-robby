package hr.fer.zemris.projekt.algorithms.neural;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

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

    RealVector calculateOutput() {
        if (context != null) {
            values = values.add(context.getValues());
        }

        RealVector result = Utils.map(values, activationFunction);

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

        setNext(layer, Utils.createRandomMatrix(values.getDimension(), layer.numberOfNeurons(), -1, 1));
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
}
