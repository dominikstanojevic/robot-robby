package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions.QFunction;
import hr.fer.zemris.projekt.grid.Field;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Agent implements Robot {

    private QFunction qFunction;
    private double currStandardFitness;

    private State lastState;
    private Move lastMove;
    private boolean lastMoveRandom;

    public Agent(QFunction qFunction) {
        this.qFunction = qFunction;
    }

    @Override
    public Move nextMove(Field current, Field left, Field right, Field up, Field down) {
        State currState = State.fromFieldStates(current, left, right, up, down);
        qFunction.updateFunction(lastState, currState, lastMove, lastMoveRandom);
        lastMoveRandom = false;
        Move move = qFunction.getMove(currState);

        if (move.equals(Move.RANDOM)) {
            Random random = ThreadLocalRandom.current();
            move = getRandomMove(random);
            lastMoveRandom = true;
        }

        lastState = currState;
        lastMove = move;

        return move;
    }

    @Override
    public double standardizedFitness() {
        return currStandardFitness;
    }

    public void setStandardFitness(double standardFitness) {
        currStandardFitness = standardFitness;
    }

    /**
     * Returns a random move from the following ones: {@link Move#UP},
     * {@link Move#DOWN}, {@link Move#LEFT}, {@link Move#RIGHT}.
     *
     * @param rnd
     *            a random number generator that is used to get a random move.
     * @return the random generated {@link Move}.
     */
    private static Move getRandomMove(Random rnd) {
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
