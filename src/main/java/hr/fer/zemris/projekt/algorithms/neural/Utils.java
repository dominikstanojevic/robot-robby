package hr.fer.zemris.projekt.algorithms.neural;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dominik on 4.12.2016..
 */
public class Utils {
    public static final Random RANDOM = ThreadLocalRandom.current();

    public static RealMatrix createRandomMatrix(int rows, int columns, double lowerBound, double upperBound) {
        double[][] matrix = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = RANDOM.nextDouble() * (upperBound - lowerBound) + lowerBound;
            }
        }

        return MatrixUtils.createRealMatrix(matrix);
    }

    public static RealVector createRandomVector(int size, double lowerBound, double upperBound) {
        double[] vector = new double[size];
        for (int i = 0; i < size; i++) {
            vector[i] = RANDOM.nextDouble() * (upperBound - lowerBound) + lowerBound;
        }

        return new ArrayRealVector(vector);
    }

    public static RealVector map(RealVector vector, ActivationFunction function) {
        double[] result = new double[vector.getDimension()];

        for (int i = 0, n = vector.getDimension(); i < n; i++) {
            result[i] = function.valueAt(vector.getEntry(i));
        }

        return new ArrayRealVector(result);
    }

    public static RealVector ebeMultiply(RealVector firstVector, RealVector secondVector) {
        if (firstVector.getDimension() != secondVector.getDimension()) {
            throw new DimensionMismatchException(firstVector.getDimension(), secondVector.getDimension());
        }

        double[] result = new double[firstVector.getDimension()];

        for (int i = 0; i < result.length; i++) {
            result[i] = firstVector.getEntry(i) * secondVector.getEntry(i);
        }

        return new ArrayRealVector(result);
    }
}
