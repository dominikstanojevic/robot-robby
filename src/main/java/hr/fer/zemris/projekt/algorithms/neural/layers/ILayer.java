package hr.fer.zemris.projekt.algorithms.neural.layers;

/**
 * Created by Dominik on 3.12.2016..
 */
public interface ILayer {
    int numberOfNeurons();
    void setFire(int index, double value);
}
