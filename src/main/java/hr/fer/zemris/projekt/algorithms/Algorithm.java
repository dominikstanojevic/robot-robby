package hr.fer.zemris.projekt.algorithms;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.grid.Field;
import hr.fer.zemris.projekt.parameter.Parameter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Defines a basic set of functionality that every algorithm should follow in
 * order to be able to play games on the simulator. Provides the functionality
 * to play a single move and to set the parameters for the algorithm.
 *
 * @author Kristijan Vulinovic
 * @version 1.0.1
 */
public interface Algorithm {
    /**
     * Calculates what move to play in the situation described by the arguments.
     *
     * @param current a {@link Field} describing the field in the grid where the
     *                robot is standing right now.
     * @param left a {@link Field} describing the field on the left side to the
     *             robot.
     * @param right a {@link Field} describing the field on the right side to the
     *             robot.
     * @param up a {@link Field} describing the field on the upper side to the
     *             robot.
     * @param down a {@link Field} describing the field on the down side to the
     *             robot.
     *
     * @return a {@link Move} that defines what the robot should do in the
     * current situation.
     */
    Move nextMove(Field current, Field left, Field right, Field up, Field down);

    /**
     * Reads all the information about the algorithm from a file. The file can
     * contain information from multiple generations of the algorithm.
     *
     * @param filePath The path of the file that contains the information for
     *                 this algorithm.
     * @throws IOException              if an I/O error is encountered when reading from the
     *                                  file
     * @throws AlgorithmFormatException if the algorithm parameters are written
     *                                  in an incorrect format
     */
    void readParametersFromFile(Path filePath) throws IOException;

    /**
     * Writes the current information about the algorithm to the file specified
     * in the argument. It is possible to write information about multiple
     * generations of the algorithm.
     *
     * @param filePath The path to the file where the information should be written.
     * @throws IOException if an I/O error is encountered when writing to a file
     */
    void writeParametersToPath(Path filePath) throws IOException;

    /**
     * Returns a list of {@link Parameter}s that are used by this algorithm.
     * Every parameter in the list contains the value that was set for the
     * algorithm, or a default value if nothing was specified.
     *
     * @return a list of all {@link Parameter}s used by the algorithm.
     */
    List<Parameter> getParameterList();

    /**
     * Changes the value of the parameter used by the algorithm to the one in
     * the argument.
     *
     * @param parameter the {@link Parameter} with the new value.
     */
    void setParameter(Parameter parameter);
}
