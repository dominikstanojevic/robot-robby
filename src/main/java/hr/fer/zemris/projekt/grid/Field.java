package hr.fer.zemris.projekt.grid;

/**
 * Represents a single field in the grid. Can be either an empty field, a wall
 * or a bottle.
 *
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public enum Field {
    /**
     * A field blocked by a wall. It is not possible to move on this field.
     */
    WALL,
    /**
     * A bottle laying on the ground. This item can be picked up.
     */
    BOTTLE,
    /**
     * An empty field.
     */
    EMPTY
}
