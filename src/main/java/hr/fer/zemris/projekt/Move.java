package hr.fer.zemris.projekt;

/**
 * Represents all the possible moves the robot can make.
 *
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public enum Move {
    /**
     * Go one field north.
     */
    UP,
    /**
     * Go one field south.
     */
    DOWN,
    /**
     * Go one field west.
     */
    LEFT,
    /**
     * Go one field east.
     */
    RIGHT,
    /**
     * Do nothing this turn.
     */
    SKIP_TURN,
    /**
     * Perform a move in a random direction.
     */
    RANDOM,
    /**
     * Collect the bottle lying on this field, if there are any bottles. If
     * there is no bottle on the field, the turn is lost.
     */
    COLLECT
}
