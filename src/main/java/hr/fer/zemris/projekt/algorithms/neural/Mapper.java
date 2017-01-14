package hr.fer.zemris.projekt.algorithms.neural;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.grid.Field;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dominik on 5.12.2016..
 */
public class Mapper {
    private static final Mapper INSTANCE = new Mapper();
    private static Map<Field, double[]> code;
    private static Map<double[], Move> decode;

    static {
        code = new HashMap<>();
        code.put(Field.BOTTLE, new double[] { 1, -1, -1 });
        code.put(Field.EMPTY, new double[] { -1, 1, -1 });
        code.put(Field.WALL, new double[] { -1, -1, 1 });

    }

    private static final int INPUT_VECTOR_SIZE = 15;
    private static final int OUTPUT_VECTOR_SIZE = Move.values().length;

    private Mapper() {
    }

    public RealVector codeInput(Field up, Field left, Field right, Field down, Field current) {
        RealVector result = new ArrayRealVector(INPUT_VECTOR_SIZE);
        int position = 0;

        checkNull(up);
        position = fillVector(result, up, position);

        checkNull(left);
        position = fillVector(result, left, position);

        checkNull(right);
        position = fillVector(result, right, position);

        checkNull(down);
        position = fillVector(result, down, position);

        checkNull(current);
        fillVector(result, current, position);

        return result;
    }

    public Move decodeOutput(RealVector output) {
        Objects.requireNonNull(output, "Neural network output cannot be null.");
        if (output.getDimension() != OUTPUT_VECTOR_SIZE) {
            throw new NeuralNetworkException("Given output has different size than expected.");
        }

        List<Integer> biggestIndexes = new ArrayList<>();
        biggestIndexes.add(0);
        double biggestValue = output.getEntry(0);
        for (int i = 1; i < OUTPUT_VECTOR_SIZE; i++) {
            double current = output.getEntry(i);

            if (current > biggestValue) {
                biggestIndexes.clear();
                biggestIndexes.add(i);
                biggestValue = current;
            } else if (current == biggestValue) {
                biggestIndexes.add(i);
            }
        }

        if (biggestIndexes.size() == 1) {
            return Move.values()[biggestIndexes.get(0)];
        } else {
            int ordinal = biggestIndexes.get(ThreadLocalRandom.current().nextInt(biggestIndexes.size()));
            return Move.values()[ordinal];
        }
    }

    private void checkNull(Field field) {
        Objects.requireNonNull(field, field.name() + " field cannot be null.");
    }

    private int fillVector(RealVector vector, Field field, int position) {
        double[] mapped = code.get(field);

        for (int i = 0; i < mapped.length; i++) {
            vector.setEntry(position + i, mapped[i]);
        }

        return position + mapped.length;
    }

    public static Mapper getInstance() {
        return INSTANCE;
    }
}
