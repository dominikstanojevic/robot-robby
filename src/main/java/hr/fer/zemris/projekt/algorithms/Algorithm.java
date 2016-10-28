package hr.fer.zemris.projekt.algorithms;

import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.Parameters;

import java.io.IOException;
import java.nio.file.Path;

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
     * Returns the default {@link Parameters parameters} for this algorithm.
     * Note that this method should return a copy of the parameters,
     * as the default values should not be changed.
     *
     * @return the default {@link Parameters parameters} for this algorithm
     */
    Parameters<? extends Algorithm> getDefaultParameters();

    /**
     * Runs the algorithm with the specified parameters.
     *
     * @param parameters parameters to run the algorithm with
     */
    void run(Parameters<? extends Algorithm> parameters);

    /**
     * Runs the algorithm with the default parameters.
     */
    default void run() {
        this.run(getDefaultParameters());
    }
}
