package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions.QFunction;
import hr.fer.zemris.projekt.grid.Field;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;

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
            move = AbstractSimulator.getRandomMove(random);
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

}
