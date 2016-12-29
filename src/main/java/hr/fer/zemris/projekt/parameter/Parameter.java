package hr.fer.zemris.projekt.parameter;

/**
 * A class representing a single parameter of an algorithm.
 *
 * @author Kristijan Vulinovic
 * @version 1.0.1
 */
public class Parameter implements Comparable<Parameter>{
    /**
     * The default value for the minimal value.
     */
    public static final double DEFAULT_MIN_VALUE = 0;
    /**
     * The default value for the maximal value.
     */
    public static final double DEFAULT_MAX_VALUE = 100;

    /**
     * The name of this parameter.
     */
    private final String name;
    /**
     * The type of this parameter.
     */
    private final ParameterType type;
    /**
     * The minimal allowed value for this parameter.
     */
    private double minValue;
    /**
     * The maximal allowed value for this parameter.
     */
    private double maxValue;
    /**
     * The value of this parameter.
     */
    private double value;

    /**
     * Creates a new parameter with the data given in the arguments.
     *
     * @param name The name of this parameter.
     * @param type The type of this parameter.
     * @param minValue The minimal allowed value for this parameter.
     * @param maxValue The maximal allowed value for this parameter.
     * @param value The value of this parameter.
     *
     * @throws IllegalArgumentException if the given value is not acceptable.
     */
    public Parameter(String name, ParameterType type, double minValue, double maxValue, double value) {
        if (name == null){
            throw new IllegalArgumentException("The parameter name is not allowed to be null.");
        }
        if (type == null){
            throw new IllegalArgumentException("The parameter type is not allowed to be null.");
        }

        this.name = name;
        this.type = type;
        this.minValue = minValue;

        setMaxValue(maxValue);
        setValue(value);
    }

    /**
     * Creates a new parameter with default values.
     *
     * @param name The name of this parameter.
     * @param type The type of this parameter.
     */
    public Parameter(String name, ParameterType type) {
        this(name, type, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, DEFAULT_MIN_VALUE);
    }

    /**
     * Returns the name of this parameter.
     *
     * @return The name of this parameter.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of this parameter.
     *
     * @return The type of this parameter.
     */
    public ParameterType getType() {
        return type;
    }

    /**
     * Returns the minimal allowed value for this parameter.
     *
     * @return The minimal allowed value for this parameter.
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * Sets the minimal allowed value for this parameter to the one given in
     * the argument.
     *
     * @param minValue the new minimal allowed value of the parameter.
     *
     * @throws IllegalArgumentException if it is not possible to use the new minimal value.
     */
    public void setMinValue(double minValue) {
        if (minValue > value){
            throw new IllegalArgumentException(
                    "The given minimal value is greater than the current value. The current value is "
                            + value + " but you provided " + minValue
            );
        }
        if (minValue > maxValue){
            throw new IllegalArgumentException(
                    "The given minimal value is greater than the current maximal value. The current maximal value is "
                            + maxValue + " but you provided " + minValue
            );
        }

        this.minValue = minValue;
    }

    /**
     * Returns the maximal allowed value for this parameter.
     *
     * @return The maximal allowed value for this parameter.
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the maximal allowed value for this parameter to the one given in
     * the argument.
     *
     * @param maxValue the new maximal allowed value of the parameter.
     *
     * @throws IllegalArgumentException if it is not possible to use the new maximal value.
     */
    public void setMaxValue(double maxValue) {
        if (maxValue < value){
            throw new IllegalArgumentException(
                    "The given maximal value is smaller than the current value. The current value is "
                    + value + " but you provided " + maxValue
            );
        }
        if (maxValue < minValue){
            throw new IllegalArgumentException(
                    "The given maximal value is smaller than the current minimal value. The current minimal value is "
                            + minValue + " but you provided " + maxValue
            );
        }

        this.maxValue = maxValue;
    }

    /**
     * Returns the current value of the parameter.
     *
     * @return The value of the parameter.
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the parameter to the one given in the argument.
     *
     * @param value The new value of the parameter.
     *
     * @throws IllegalArgumentException if the given value is not inside the
     * allowed interval.
     */
    public void setValue(double value) {
        if (value < minValue){
            throw new IllegalArgumentException("The given value is too small! The minimal allowed value is " +
                    minValue + " but the given value was " + value);
        }
        if (value > maxValue){
            throw new IllegalArgumentException("The given value is too big! The maximal allowed value is " +
                    maxValue + " but the given value was " + value);
        }

        this.value = value;
    }

	@Override
	public int compareTo(Parameter o) {
		return this.name.compareTo(o.name);
	}
}
