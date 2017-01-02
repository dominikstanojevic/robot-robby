package hr.fer.zemris.projekt.observer.observations;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.grid.IGrid;

/**
 * Represents the robot's action on a {@link hr.fer.zemris.projekt.grid.Grid
 * grid}. Contains a reference to the {@code Grid} that the {@code Move} was
 * played on, as well as the previous and current positions of the robot.
 *
 * @author Leon Luttenberger
 * @version 1.0.0
 */
public class RobotActionTaken {

    /**
     * Grid where the action was taken.
     */
    private IGrid grid;

    /**
     * Action taken.
     */
    private Move move;

    /**
     * Previous X position of the robot, before the action was taken.
     */
    private int previousRow;

    /**
     * Previous Y position of the robot, before the action was taken.
     */
    private int previousColumn;

    /**
     * Current X position of the robot, after the action was taken.
     */
    private int currentRow;

    /**
     * Current Y position of the robot, after the action was taken.
     */
    private int currentColumn;

    /**
     * Constructs a {@link RobotActionTaken} object with the specified
     * parameters.
     *
     * @param grid
     *            grid where the action was taken
     * @param move
     *            action taken
     * @param previousRow
     *            previous X position of the robot, before the action was taken
     * @param previousColumn
     *            previous Y position of the robot, before the action was taken
     * @param currentRow
     *            previous X position of the robot, after the action was taken
     * @param currentColumn
     *            previous Y position of the robot, after the action was taken
     */
    public RobotActionTaken(IGrid grid, Move move, int previousRow, int previousColumn,
            int currentRow, int currentColumn) {
        this.grid = grid;
        this.move = move;
        this.previousRow = previousRow;
        this.previousColumn = previousColumn;
        this.currentRow = currentRow;
        this.currentColumn = currentColumn;
    }

    /**
     * Returns the grid where the action was taken.
     *
     * @return the grid where the action was taken
     */
    public IGrid getGrid() {
        return grid;
    }

    /**
     * Returns the action taken by the robot.
     *
     * @return the action taken by the robot
     */
    public Move getMove() {
        return move;
    }

    /**
     * Returns the new X coordinate of the robot, before the action was taken.
     *
     * @return the new X coordinate of the robot, before the action was taken
     */
    public int getPreviousRow() {
        return previousRow;
    }

    /**
     * Returns the new Y coordinate of the robot, before the action was taken.
     *
     * @return the new Y coordinate of the robot, before the action was taken
     */

    public int getPreviousColumn() {
        return previousColumn;
    }

    /**
     * Returns the new X coordinate of the robot, after the action was taken.
     *
     * @return the new X coordinate of the robot, after the action was taken
     */

    public int getCurrentRow() {
        return currentRow;
    }

    /**
     * Returns the new Y coordinate of the robot, after the action was taken.
     *
     * @return the new Y coordinate of the robot, after the action was taken
     */

    public int getCurrentColumn() {
        return currentColumn;
    }
}
