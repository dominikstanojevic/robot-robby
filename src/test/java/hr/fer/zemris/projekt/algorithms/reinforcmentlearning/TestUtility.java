package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.Move;

import java.util.Random;

public class TestUtility {
    public static Move getRandomMove(Random rnd) {
        Move nextMove = null;

        int moveID = rnd.nextInt(4);
        switch (moveID) {
        case 0:
            nextMove = Move.UP;
            break;
        case 1:
            nextMove = Move.DOWN;
            break;
        case 2:
            nextMove = Move.LEFT;
            break;
        case 3:
            nextMove = Move.RIGHT;
            break;
        }

        return nextMove;
    }
}
