package hr.fer.zemris.projekt.algorithms;

import hr.fer.zemris.projekt.grid.GridFormatException;

/**
 * Exception thrown when an application attempts to convert the contents of a file or string into the
 * appropriate {@link Algorithm} implementation, but the file/string does not have the appropriate format.
 *
 * @author Leon Luttenberger
 * @version 1.0.0
 */
public class AlgorithmFormatException extends IllegalArgumentException {

    /**
     * Constructs a {@link AlgorithmFormatException} with the default message.
     */
    public AlgorithmFormatException() {
    }

    /**
     * Constructs a {@link GridFormatException} with the specified message.
     *
     * @param message message of the exception
     */
    public AlgorithmFormatException(String message) {
        super(message);
    }

    /**
     * Constructs a {@link GridFormatException} with the specified message and
     * cause.
     *
     * @param message message of the exception
     * @param cause   cause of the exception
     */
    public AlgorithmFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@link GridFormatException} with the specified cause and it's
     * message.
     *
     * @param cause cause of the exception
     */
    public AlgorithmFormatException(Throwable cause) {
        super(cause);
    }
}
