package hr.fer.zemris.projekt.simulator;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.grid.IGrid;

import java.util.List;

/**
 * A class that holds the statistical information about a single game played by
 * an algorithm.
 *
 * @author Kristijan Vulinovic
 * @version 1.0.1
 */
public class Stats {
    /**
     * The number of moves needed to collect every bottle from the grid, or the
     * total number of moves played.
     */
    private final int movesNeeded;
    /**
     * The total number of collected bottles.
     */
    private final int bottlesCollected;
    /**
     * The number of bottles left on the grid.
     */
    private final int bottlesLeft;
    /**
     * The number of moves that resulted in hitting a wall during the whole
     * game.
     */
    private final int wallsHit;
    /**
     * Number of moves where the robot tried to collect a bottle, but there was
     * no bottle on the current field.
     */
    private final int emptyPickups;

    /**
     * The grid that was used to play the game.
     */
    private final IGrid grid;
    /**
     * A list of all the moves made in the game.
     */
    private final List<Move> moves;

    /**
     * Creates the statistics for a new game, with all the detail about the
     * game.
     *
     * @param movesNeeded
     *            The number of moves needed to collect every bottle from the
     *            grid, or the total number of moves played.
     * @param bottlesCollected
     *            The total number of collected bottles.
     * @param bottlesLeft
     *            The number of bottles left on the grid.
     * @param wallsHit
     *            The number of moves that resulted in hitting a wall during the
     *            whole game.
     * @param emptyPickups
     *            Number of moves where the robot tried to collect a bottle, but
     *            there was no bottle on the current field.
     * @param grid
     *            The grid that was used to play the game.
     * @param moves
     *            A list of all the moves made in the game.
     */
    public Stats(int movesNeeded, int bottlesCollected, int bottlesLeft, int wallsHit,
            int emptyPickups, IGrid grid, List<Move> moves) {

        if (grid == null) {
            throw new IllegalArgumentException("The given grid is not allowed to be null.");
        }
        if (moves == null) {
            throw new IllegalArgumentException("The given list of moves is not allowed to be null.");
        }

        this.movesNeeded = movesNeeded;
        this.bottlesCollected = bottlesCollected;
        this.bottlesLeft = bottlesLeft;
        this.wallsHit = wallsHit;
        this.emptyPickups = emptyPickups;
        this.grid = grid;
        this.moves = moves;
    }

    /**
     * Returns the number of moves needed to collect every bottle from the grid,
     * or the total number of moves played.
     *
     * @return The number of moves needed to collect every bottle from the grid,
     *         or the total number of moves played.
     */
    public int getMovesNeeded() {
        return movesNeeded;
    }

    /**
     * Returns the total number of collected bottles.
     *
     * @return The total number of collected bottles.
     */
    public int getBottlesCollected() {
        return bottlesCollected;
    }

    /**
     * Returns the number of bottles left on the grid.
     *
     * @return The number of bottles left on the grid.
     */
    public int getBottlesLeft() {
        return bottlesLeft;
    }

    /**
     * Returns the number of moves that resulted in hitting a wall during the
     * whole game.
     *
     * @return The number of moves that resulted in hitting a wall during the
     *         whole game.
     */
    public int getWallsHit() {
        return wallsHit;
    }

    /**
     * Returns number of moves where the robot tried to collect a bottle, but
     * there was no bottle on the current field.
     *
     * @return Number of moves where the robot tried to collect a bottle, but
     *         there was no bottle on the current field.
     */
    public int getEmptyPickups() {
        return emptyPickups;
    }

    /**
     * Returns the grid that was used to play the game.
     *
     * @return The grid that was used to play the game.
     */
    public IGrid getGrid() {
        return grid;
    }

    /**
     * Returns a list of all the moves made in the game.
     *
     * @return A list of all the moves made in the game.
     */
    public List<Move> getMoves() {
        return moves;
    }
}
