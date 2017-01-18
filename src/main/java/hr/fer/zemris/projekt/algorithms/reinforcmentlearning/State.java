package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.grid.Field;

import java.util.HashMap;
import java.util.Map;

/**
 * Class models robot state with states of robot's current field and neighbor
 * fields.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 15.1.2017.
 */
public class State {

    /**
     * Map which maps inner state positions to field states.
     */
    private Map<StateFields, Field> groupFieldStates;

    /**
     * Number of state fields.
     */
    public static final int DEFAULT_STATE_FIELD_NUMBER = 5;

    /**
     * Enumeration which describe inner state position.
     *
     * @author Domagoj Pluscec
     * @version v1.0, 15.1.2017.
     */
    public enum StateFields {
        /**
         * Current field.
         */
        CURRENT,
        /**
         * Left field.
         */
        LEFT,
        /**
         * Right field.
         */
        RIGHT,
        /**
         * Upper field.
         */
        UP,
        /**
         * Down field.
         */
        DOWN;

    }

    /**
     * Constructor which initializes empty state.
     */
    public State() {
        groupFieldStates = new HashMap<>(DEFAULT_STATE_FIELD_NUMBER);
    }

    /**
     * Method adds field to the current state at given state field position.
     *
     * @param field
     *            state field position
     * @param fieldState
     *            field state
     */
    public void addFieldState(StateFields field, Field fieldState) {
        groupFieldStates.put(field, fieldState);
    }

    /**
     * Method obtains field state at given position.
     *
     * @param field
     *            field position
     * @return field state
     */
    public Field getFieldState(StateFields field) {
        return groupFieldStates.get(field);
    }

    /**
     * Factory method that constructs state from field states.
     *
     * @param current
     *            current field state
     * @param left
     *            left field state
     * @param right
     *            right field state
     * @param up
     *            upper field state
     * @param down
     *            down field state
     * @return initialized state
     */
    public static State fromFieldStates(Field current, Field left, Field right, Field up, Field down) {
        State state = new State();
        state.addFieldState(StateFields.CURRENT, current);
        state.addFieldState(StateFields.LEFT, left);
        state.addFieldState(StateFields.RIGHT, right);
        state.addFieldState(StateFields.UP, up);
        state.addFieldState(StateFields.DOWN, down);
        return state;
    }

    @Override
    public String toString() {
        return getFieldState(StateFields.CURRENT) + " " + getFieldState(StateFields.LEFT) + " "
                + getFieldState(StateFields.RIGHT) + " " + getFieldState(StateFields.UP) + " "
                + getFieldState(StateFields.DOWN);
    }

    /**
     * Method initializes state from given state string. Format of the string is
     * field states separated by space. For example: BOTTLE BOTTLE EMPTY EMPTY
     * BOTTLE.
     *
     * @param stateString
     *            string containing default state field number of fields
     * @return initialized state
     * @throws IllegalArgumentException
     *             if the string is bad formated or if the string has illegal
     *             number of states
     */
    public static State fromString(String stateString) {
        String[] fields = stateString.split(" ");
        if (fields.length != DEFAULT_STATE_FIELD_NUMBER) {
            throw new IllegalArgumentException("Illegal state string");
        }
        return fromFieldStates(fieldFromString(fields[0]), fieldFromString(fields[1]),
                fieldFromString(fields[2]), fieldFromString(fields[3]), fieldFromString(fields[4]));
    }

    /**
     * Generates field from string. Field string should be in format "BOTTLE",
     * "EMPTY" or "WALL".
     *
     * @param fieldString
     *            string containing field. string should be trimmed
     * @return initialized field
     */
    private static Field fieldFromString(String fieldString) {
        for (Field field : Field.values()) {
            if (fieldString.equals(field.toString())) {
                return field;
            }
        }
        // TODO
        System.out.println(fieldString);
        throw new IllegalArgumentException("Illegal fieldString ");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupFieldStates == null) ? 0 : groupFieldStates.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        State other = (State) obj;
        if (groupFieldStates == null) {
            if (other.groupFieldStates != null) {
                return false;
            }
        } else if (!groupFieldStates.equals(other.groupFieldStates)) {
            return false;
        }
        return true;
    }

}
