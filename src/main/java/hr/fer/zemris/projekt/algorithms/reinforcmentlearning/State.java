package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.grid.Field;

import java.util.HashMap;
import java.util.Map;

public class State {

    private Map<StateFields, Field> groupFieldStates;

    public static final int DEFAULT_STATE_FIELD_NUMBER = 5;

    public static enum StateFields {
        CURRENT, LEFT, RIGHT, UP, DOWN;

    }

    public State() {
        groupFieldStates = new HashMap<>(DEFAULT_STATE_FIELD_NUMBER);
    }

    public void addFieldState(StateFields field, Field fieldState) {
        groupFieldStates.put(field, fieldState);
    }

    public Field getFieldState(StateFields field) {
        return groupFieldStates.get(field);
    }

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

    public static State fromString(String stateString) {
        String[] fields = stateString.split(" ");
        if (fields.length != 5) {
            throw new IllegalArgumentException("Illegal state string");
        }
        return fromFieldStates(fieldFromString(fields[0]), fieldFromString(fields[1]),
                fieldFromString(fields[2]), fieldFromString(fields[3]), fieldFromString(fields[4]));
    }

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
