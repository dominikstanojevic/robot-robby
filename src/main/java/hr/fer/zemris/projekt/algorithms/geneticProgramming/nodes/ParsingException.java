package hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes;

/**
 * Exception thrown by {@link ProgramTreeParser} if an error occurs during
 * parsing.
 * 
 * @author Dunja Vesinger
 * @version 1.0.0
 */
public class ParsingException extends Exception {

    /**
     * Number which Java uses for serialization.
     */
    private static final long serialVersionUID = -9056173061071142841L;

    /**
     * Creates a new ParsingException.
     */
    public ParsingException() {
        super();
    }

    /**
     * Creates a new ParsingException with the given message.
     * 
     * @param message
     *            exception message
     */
    public ParsingException(String message) {
        super(message);
    }

}
