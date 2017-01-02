package hr.fer.zemris.projekt.grid;

/**
 * Exception thrown when an application attempts to convert the contents of a
 * file or string into an {@link IGrid}, but the file/string does not have the
 * appropriate format.
 *
 * @author Leon Luttenberger
 * @version 1.0.0
 */
public class GridFormatException extends IllegalArgumentException {

    /**
     * Constructs a {@link GridFormatException} with the default message.
     */
    public GridFormatException() {
    }

    /**
     * Constructs a {@link GridFormatException} with the specified message.
     *
     * @param message
     *            message of the exception
     */
    public GridFormatException(String message) {
        super(message);
    }

    /**
     * Constructs a {@link GridFormatException} with the specified message and
     * cause.
     *
     * @param message
     *            message of the exception
     * @param cause
     *            cause of the exception
     */
    public GridFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@link GridFormatException} with the specified cause and
     * it's message.
     *
     * @param cause
     *            cause of the exception
     */
    public GridFormatException(Throwable cause) {
        super(cause);
    }
}
