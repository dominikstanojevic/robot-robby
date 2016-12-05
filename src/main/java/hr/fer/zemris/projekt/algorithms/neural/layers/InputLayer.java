package hr.fer.zemris.projekt.algorithms.neural.layers;

import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dominik on 3.12.2016..
 */
public class InputLayer implements ILayer {
    private RealVector inputs;

    @Override
    public int numberOfNeurons() {
        return 0;
    }

    @Override
    public void setFire(int index, double value) {

    }
}
