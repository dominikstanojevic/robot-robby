package hr.fer.zemris.projekt.algorithms;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.grid.Field;

public interface Robot {

    /**
     * Calculates what move to play in the situation described by the arguments.
     *
     * @param current a {@link Field} describing the field in the grid where the
     *                robot is standing right now.
     * @param left    a {@link Field} describing the field on the left side to the
     *                robot.
     * @param right   a {@link Field} describing the field on the right side to the
     *                robot.
     * @param up      a {@link Field} describing the field on the upper side to the
     *                robot.
     * @param down    a {@link Field} describing the field on the down side to the
     *                robot.
     * @return a {@link Move} that defines what the robot should do in the
     * current situation.
     */
    Move nextMove(Field current, Field left, Field right, Field up, Field down);
}
