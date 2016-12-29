package hr.fer.zemris.projekt.web.dummy;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.grid.Field;

import java.util.Random;

/**
 * A robot that makes only random moves. Used for testing purposes.
 *
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class RandomRobot implements Robot {
    private static Random rnd = new Random();

    @Override
    public Move nextMove(Field current, Field left, Field right, Field up, Field down) {
        int x = rnd.nextInt(6);
        if (x == 0) return Move.UP;
        if (x == 1) return Move.COLLECT;
        if (x == 2) return Move.DOWN;
        if (x == 3) return Move.LEFT;
        if (x == 4) return Move.RIGHT;
        if (x == 5) return Move.SKIP_TURN;

        return null;
    }

    @Override
    public double standardizedFitness() {
        return 0;
    }
}
