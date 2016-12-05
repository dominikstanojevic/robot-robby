package hr.fer.zemris.projekt.algorithms.neural.layers;

import hr.fer.zemris.projekt.algorithms.neural.NeuralNetworkException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dominik on 3.12.2016..
 */
public class OutputLayer implements ILayer {
    private RealVector outputs;

    public OutputLayer(int size) {
        if(size < 0) {
            throw new NeuralNetworkException("Size of output layer cannot be zero.");
        }

        this.outputs = new ArrayRealVector(size);
    }

    public RealVector getOutputs() {
        return new ArrayRealVector(outputs);
    }

    @Override
    public int numberOfNeurons() {
        return outputs.getDimension();
    }

    @Override
    public void setFire(int index, double value) {
        if (index < 0 || index >= outputs.getDimension()) {
            throw new NeuralNetworkException(
                    "Given index is not valid for this layer. Layer consists of " + outputs.getDimension() +
                    " neurons.");
        }
        outputs.setEntry(index, value);
    }
}
