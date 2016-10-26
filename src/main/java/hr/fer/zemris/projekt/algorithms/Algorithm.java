package hr.fer.zemris.projekt.algorithms;

import hr.fer.zemris.projekt.parameter.Parameter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * <p>Defines a basic set of functionality that every {@link Robot} training
 * algorithm should implement in order to be used properly from the program.</p>
 * <p>The needed functionality is the following:</p>
 * <ul>
 *     <li>Parsing it's appropriate {@code Robot} from a file.</li>
 *     <li>Writing it's appropriate {@code Robot} to a file.</li>
 *     <li>Setting {@link Parameter parameters} for the algorithm.</li>
 *     <li>Retrieving the list of {@link Parameter parameters}.</li>
 * </ul>
 *
 * @author Kristijan Vulinovic, Leon Luttenberger
 * @version 1.1.0
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
