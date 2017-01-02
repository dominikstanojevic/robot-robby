package hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.grid.Field;

import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * An implementation of the {@link Node} interface which represents a terminal
 * node of the program tree. Each terminal node contains one {@link Move} and it
 * always evaluates to this move.
 * </p>
 * 
 * @author Dunja Vesinger
 * @version 1.0.0
 */
public class TerminalNode implements Node {
    /**
     * Move to be taken.
     */
    private Move move;

    /**
     * Creates a new TerminalNode with the given move.
     * 
     * @param move
     */
    public TerminalNode(Move move) {
        this.move = move;
    }

    @Override
    public Move evaluate(Field current, Field left, Field right, Field up, Field down) {
        return move;
    }

    @Override
    public int getDepth() {
        return 1;
    }

    /**
     * Returns a random TerminalNode containing one {@link Move}. Each move has
     * an equal probability of being selected.
     * 
     * @return
     */
    public static Node randomTerminalNode() {

        Move[] moves = Move.values();
        int random = ThreadLocalRandom.current().nextInt(0, moves.length);

        return new TerminalNode(moves[random]);
    }

    @Override
    public String toString() {
        return move.toString();
    }

    @Override
    public Node copy() {
        return new TerminalNode(move);
    }

}
