package hr.fer.zemris.projekt.algorithms;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.grid.Field;

/**
 * <p>
 * Represents an implementation of Robby the Robot's brain. The robot
 * implementation can be asked for it's next move depending on it's current
 * surrounding using the
 * {@link Robot#nextMove(Field, Field, Field, Field, Field) nextMove} method.
 * </p>
 *
 * <p>
 * This implementation of the robot's mind can range from being a single
 * chromosome in a genetic algorithm to a neural network, or any other robot
 * training algorithm.
 * </p>
 *
 * @author Leon Luttenberger
 * @version 1.0.0
 */
public interface Robot {

    /**
     * Calculates what move to play in the situation described by the arguments.
     *
     * @param current
     *            a {@link Field} describing the field in the grid where the
     *            robot is standing right now.
     * @param left
     *            a {@link Field} describing the field on the left side to the
     *            robot.
     * @param right
     *            a {@link Field} describing the field on the right side to the
     *            robot.
     * @param up
     *            a {@link Field} describing the field on the upper side to the
     *            robot.
     * @param down
     *            a {@link Field} describing the field on the down side to the
     *            robot.
     * @return a {@link Move} that defines what the robot should do in the
     *         current situation.
     */
    Move nextMove(Field current, Field left, Field right, Field up, Field down);

    /**
     * Returns the standardized fitness value for the {@code Robot}.
     *
     * @return the standardized fitness value for the {@code Robot}
     */
    double standardizedFitness();
}
