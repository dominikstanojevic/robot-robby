package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions.QFunction;
import hr.fer.zemris.projekt.grid.Field;

public class Agent implements Robot {

    private QFunction qFunction;

    private State lastState;
    private Move lastMove;

    public Agent(QFunction qFunction) {
        this.qFunction = qFunction;
    }

    @Override
    public Move nextMove(Field current, Field left, Field right, Field up, Field down) {
        State currState = State.fromFieldStates(current, left, right, up, down);

        qFunction.updateFunction(lastState, currState, lastMove);
        Move move = qFunction.getMove(currState);

        lastState = currState;
        lastMove = move;

        return move;
    }
}
