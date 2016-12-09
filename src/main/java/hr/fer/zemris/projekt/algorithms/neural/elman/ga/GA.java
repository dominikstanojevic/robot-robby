package hr.fer.zemris.projekt.algorithms.neural.elman.ga;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created by Dominik on 9.12.2016..
 */
public class GA implements Algorithm {
    @Override
    public Robot readSolutionFromFile(Path filePath) throws IOException {
        return null;
    }

    @Override
    public void writeSolutionToFile(Path filePath, Robot robot) throws IOException {
        Objects.requireNonNull(robot, "Robot cannot be null.");
        if (!(robot instanceof ElmanNeuralNetwork)) {
            throw new IllegalArgumentException("Given implementation is not correct. Expected Elman neural network.");
        }

        StringJoiner sj = new StringJoiner(" ");
        for (double weight : ((ElmanNeuralNetwork) robot).getWeights()) {
            sj.add(Double.toString(weight));
        }

        Files.write(filePath, sj.toString().getBytes());
    }

    @Override
    public Parameters<? extends Algorithm> getDefaultParameters() {
        return null;
    }

    @Override
    public void run(
            AbstractSimulator simulator, Parameters<? extends Algorithm> parameters) {

    }
}
