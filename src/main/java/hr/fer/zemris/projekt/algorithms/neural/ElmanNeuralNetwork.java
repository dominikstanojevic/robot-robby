package hr.fer.zemris.projekt.algorithms.neural;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;

/**
 * Created by Dominik on 5.12.2016..
 */
public class ElmanNeuralNetwork {
    private Layer[] layers = new Layer[3];

    public ElmanNeuralNetwork(int inputLayerSize, int hiddenLayerSize, int outputLayerSize) {
        layers[0] = new Layer(inputLayerSize);
        layers[1] = new Layer(hiddenLayerSize);
        layers[1].activateContextLayer();
        layers[2] = new Layer(outputLayerSize);

        layers[0].setNext(layers[1]);
        layers[1].setNext(layers[2]);
    }

    private RealVector calculate(RealVector input) {
        layers[0].giveInput(input);

        RealVector output = null;
        for(Layer layer : layers) {
            output = layer.calculateOutput();
        }

        return output;
    }

    public static void main(String[] args) {
        ElmanNeuralNetwork enn = new ElmanNeuralNetwork(15, 15, 7);
        double[] i = new double[15];
        Arrays.fill(i, 1);
        RealVector input = new ArrayRealVector(i);
        RealVector output = enn.calculate(input);
        System.out.println(output);
    }
}
