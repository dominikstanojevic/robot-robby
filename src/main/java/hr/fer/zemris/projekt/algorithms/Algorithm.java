package hr.fer.zemris.projekt.algorithms;

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
     * Reads all the information about the algorithm from a file. The file can
     * contain information from multiple generations of the algorithm.
     *
     * @param filePath The path of the file that contains the information for
     *                 this algorithm.
     * @throws IOException              if an I/O error is encountered when reading from the
     *                                  file
     * @throws RobotFormatException if the algorithm parameters are written
     *                                  in an incorrect format
     */
    Robot readSolutionFromFile(Path filePath) throws IOException;

    /**
     * Writes the current information about the algorithm to the file specified
     * in the argument. It is possible to write information about multiple
     * generations of the algorithm.
     *
     * @param filePath The path to the file where the information should be written.
     * @throws IOException if an I/O error is encountered when writing to a file
     */
    void writeSolutionToFile(Path filePath, Robot robot) throws IOException;

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
