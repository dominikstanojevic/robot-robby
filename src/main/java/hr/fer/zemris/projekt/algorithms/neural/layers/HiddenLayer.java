package hr.fer.zemris.projekt.algorithms.neural.layers;

import hr.fer.zemris.projekt.algorithms.neural.ActivationFunction;
import hr.fer.zemris.projekt.algorithms.neural.NeuralNetworkException;
import hr.fer.zemris.projekt.algorithms.neural.Utils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Objects;

/**
 * Created by Dominik on 3.12.2016..
 */
public class HiddenLayer implements ILayer {
    private RealVector values;
    private ActivationFunction activationFunction;

    private ILayer nextLayer;
    private RealMatrix weights;

    private ContextLayer contextLayer;

    public HiddenLayer(int numberOfNeurons, ActivationFunction activationFunction) {
        values = new ArrayRealVector(numberOfNeurons);
        this.activationFunction = activationFunction;
    }

    public void feed() {
        if (contextLayer != null) {
            values = values.add(contextLayer.feedValues());
        }

        for (int i = 0, n = nextLayer.numberOfNeurons(); i < n; i++) {
            RealVector weightsForNeuron = weights.getColumnVector(i);
            double total = values.dotProduct(weightsForNeuron);

            nextLayer.setFire(i, );
        }

        if (contextLayer != null) {
            contextLayer.copyValues(values);
        }
    }

    public void addContextLayer() {
        if (contextLayer == null) {
            contextLayer = new ContextLayer(values.getDimension());
        } else {
            throw new RuntimeException("Context layer already created.");
        }
    }

    public void addNextLayer(ILayer layer) {
        addNextLayer(layer, Utils.createRandomMatrix(values.getDimension(), layer.numberOfNeurons(), -1, 1));
    }

    public void addNextLayer(ILayer layer, RealMatrix matrix) {
        Objects.requireNonNull(layer);
        Objects.requireNonNull(matrix);
        if (values.getDimension() != matrix.getRowDimension()) {
            throw new NeuralNetworkException("Number of neurons in this layer and the weights row does not match.");
        }
        if (layer.numberOfNeurons() != matrix.getColumnDimension()) {
            throw new NeuralNetworkException(
                    "Number of neurons in the next layer an the weights column does not " + "match");
        }

        nextLayer = layer;
        weights = matrix;
    }

    @Override
    public int numberOfNeurons() {
        return values.getDimension();
    }

    @Override
    public void setFire(int index, double value) {
        values.setEntry(index, value);
    }
}
