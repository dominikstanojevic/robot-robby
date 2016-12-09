package hr.fer.zemris.projekt.algorithms.neural.elman.ga;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.neural.ActivationFunction;
import hr.fer.zemris.projekt.algorithms.neural.Layer;
import hr.fer.zemris.projekt.algorithms.neural.Mapper;
import hr.fer.zemris.projekt.algorithms.neural.NeuralNetworkException;
import hr.fer.zemris.projekt.grid.Field;
import org.apache.commons.math3.linear.RealVector;

import java.util.Objects;

/**
 * Created by Dominik on 9.12.2016..
 */
public class ElmanNeuralNetwork implements Robot {
    private Layer[] layers = new Layer[3];
    private int numberOfWeights;

    public ElmanNeuralNetwork(int[] neuronsPerLayer, ActivationFunction[] functions) {
        Objects.requireNonNull(neuronsPerLayer, "Array containing number of neurons per layer cannot be null.");
        Objects.requireNonNull(functions, "Array containing activation functions cannot be null.");

        if (neuronsPerLayer.length != 3) {
            throw new NeuralNetworkException(
                    "Invalid length for array containing number of neurons. Expected length " + "is 3.");
        }
        if (functions.length != 2) {
            throw new NeuralNetworkException(
                    "Invalid length for array containing activation functions. Expected " + "length is 2.");
        }

        layers[0] = new Layer(neuronsPerLayer[0]);
        layers[1] = new Layer(neuronsPerLayer[1], functions[0]);
        layers[2] = new Layer(neuronsPerLayer[2], functions[1]);

        layers[0].setNext(layers[1]);
        layers[1].setNext(layers[2]);
        layers[1].activateContextLayer();

        numberOfWeights = calculateNumberOfWeights();
    }

    private int calculateNumberOfWeights() {
        int total = 0;
        for (Layer layer : layers) {
            total += layer.numberOfWeights();
        }

        return total;
    }

    @Override
    public Move nextMove(
            Field current, Field left, Field right, Field up, Field down) {
        RealVector input = Mapper.getInstance().codeInput(up, left, right, down, current);

        RealVector output = calculateOutput(input);
        return Mapper.getInstance().decodeOutput(output);
    }

    private RealVector calculateOutput(RealVector input) {
        layers[0].giveInput(input);

        RealVector output = null;
        for (Layer layer : layers) {
            output = layer.calculateOutput();
        }

        return output;
    }

    public void clearContext() {
        for (Layer layer : layers) {
            layer.clearContext();
        }
    }

    public double[] getWeights() {
        double[] weights = new double[numberOfWeights];

        int start = 0;
        for(Layer layer : layers) {
            double[] layerWeights = layer.getWeights();
            System.arraycopy(layerWeights, 0, weights, start, layerWeights.length);
            start += layerWeights.length;
        }

        return weights;
    }
}
