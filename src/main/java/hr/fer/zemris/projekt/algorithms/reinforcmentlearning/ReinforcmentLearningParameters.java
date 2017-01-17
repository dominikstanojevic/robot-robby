package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.ParameterType;
import hr.fer.zemris.projekt.parameter.Parameters;

import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * Parameters for reinforcment learning algorithm.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 15.1.2017.
 */
public class ReinforcmentLearningParameters implements Parameters<ReinforcmentLearningAlgorithm> {

    /**
     * ID of the ${code stop threshold} parameter.
     */
    public static final String STOP_THRESHOLD_ID = "Stop threshold";

    /**
     * Default value of the ${code stop threshold} parameter.
     */
    public static final int DEFAULT_THRESHOLD = 1;

    /**
     * Living reward parameter name.
     */
    public static final String LIVING_REWARD_NAME = "Leaving reward";
    /**
     * Default living reward.
     */
    private static final double DEFAULT_LIVING_REWARD = -2;
    /**
     * Minimal living reward.
     */
    private static final double MIN_LIVING_REWARD = -100;
    /**
     * Maximal living reward.
     */
    private static final double MAX_LIVING_REWARD = 100;

    /**
     * Picked bottle reward parameter name.
     */
    public static final String PICKED_BOTTLE_REWARD_NAME = "Prize for picking up a bottle";
    /**
     * Default picked bottle reward.
     */
    private static final double DEFAULT_PICKED_BOTTLE_REWARD = 10;
    /**
     * Minimal picked bottle reward.
     */
    private static final double MIN_PICKED_BOTTLE_REWARD = -100;
    /**
     * Maximal picked bottle reward.
     */
    private static final double MAX_PICKED_BOTTLE_REWARD = 100;

    /**
     * Empty pickup reward parameter name.
     */
    public static final String EMPTY_PICK_UP_REWARD_NAME = "Penalty for empty pickup";
    /**
     * Default empty pickup reward.
     */
    private static final double DEFAULT_EMPTY_PICK_UP_REWARD = -1;
    /**
     * Minimal empty pickup reward.
     */
    private static final double MIN_EMPTY_PICK_UP_REWARD = -100;
    /**
     * Maximal empty pickup reward.
     */
    private static final double MAX_EMPTY_PICK_UP_REWARD = 100;

    /**
     * Wall collision reward parameter name.
     */
    public static final String WALL_COLLISION_REWARD_NAME = "Penalty for hitting a wall";
    /**
     * Default wall collision reward.
     */
    private static final double DEFAULT_WALL_COLLISION_REWARD = -5;
    /**
     * Minimal wall collision reward.
     */
    private static final double MIN_WALL_COLLISION_REWARD = -1000;
    /**
     * Maximal wall collision reward.
     */
    private static final double MAX_WALL_COLLISION_REWARD = 1000;

    /**
     * Learning rate parameter name.
     */
    public static final String LEARNING_RATE_NAME = "Learning rate";
    /**
     * Default learning rate value.
     */
    private static final double DEAFULT_LEARNING_RATE = 0.15;
    /**
     * Minimal learning rate value.
     */
    private static final double MIN_LEARNING_RATE = 0;
    /**
     * Maximal learning rate value.
     */
    private static final double MAX_LEARNING_RATE = 1;

    /**
     * Future reward discount factor parameter name.
     */
    public static final String FUTURE_DISCOUNT_FACTOR_NAME = "Future reward discount factor";
    /**
     * Default future reward discount factor.
     */
    private static final double DEFAULT_FUTURE_DISCOUNT_FACTOR = 0.5;
    /**
     * Minimal future reward discount factor.
     */
    private static final double MIN_FUTURE_DISCOUNT_FACTOR = 0;
    /**
     * Maximal future reward discount factor.
     */
    private static final double MAX_FUTURE_DISCOUNT_FACTOR = 1;

    /**
     * Exploration factor parameter name.
     */
    public static final String EXPLORATION_FACTOR_NAME = "Exploration factor";
    /**
     * Default exploration factor value.
     */
    private static final double DEFAULT_EXPLORATION_FACTOR = 2;
    /**
     * Minimal exploration factor value.
     */
    private static final double MIN_EXPLORATION_FACTOR = -1000;
    /**
     * Maximal exploration factor value.
     */
    private static final double MAX_EXPLORATION_FACTOR = 1000;

    /**
     * Number of iterations parameter name.
     */
    public static final String ITERATIONS_NUMBER_NAME = "Number of iterations";
    /**
     * Default number of iterations.
     */
    private static final int DEFAULT_ITERATIONS_NUMBER = 1000;
    /**
     * Minimal number of iterations.
     */
    private static final int MIN_ITERATIONS_NUMBER = 1;
    /**
     * Maximal number of iterations.
     */
    private static final int MAX_ITERATIONS_NUMBER = 1000000;

    /**
     * Parameters map.
     */
    private HashMap<String, Parameter> parameterMap;

    /**
     * Default parameters constructor.
     */
    public ReinforcmentLearningParameters() {
        parameterMap = new HashMap<String, Parameter>();
        parameterMap.put(STOP_THRESHOLD_ID, new Parameter(STOP_THRESHOLD_ID, ParameterType.DOUBLE,
                0, 1.0, 1.0));
        parameterMap.put(ITERATIONS_NUMBER_NAME, new Parameter(ITERATIONS_NUMBER_NAME,
                ParameterType.INTEGER, MIN_ITERATIONS_NUMBER, MAX_ITERATIONS_NUMBER,
                DEFAULT_ITERATIONS_NUMBER));
        parameterMap
        .put(LIVING_REWARD_NAME, new Parameter(LIVING_REWARD_NAME, ParameterType.INTEGER,
                MIN_LIVING_REWARD, MAX_LIVING_REWARD, DEFAULT_LIVING_REWARD));
        parameterMap.put(PICKED_BOTTLE_REWARD_NAME, new Parameter(PICKED_BOTTLE_REWARD_NAME,
                ParameterType.INTEGER, MIN_PICKED_BOTTLE_REWARD, MAX_PICKED_BOTTLE_REWARD,
                DEFAULT_PICKED_BOTTLE_REWARD));
        parameterMap.put(EMPTY_PICK_UP_REWARD_NAME, new Parameter(EMPTY_PICK_UP_REWARD_NAME,
                ParameterType.INTEGER, MIN_EMPTY_PICK_UP_REWARD, MAX_EMPTY_PICK_UP_REWARD,
                DEFAULT_EMPTY_PICK_UP_REWARD));
        parameterMap.put(WALL_COLLISION_REWARD_NAME, new Parameter(WALL_COLLISION_REWARD_NAME,
                ParameterType.INTEGER, MIN_WALL_COLLISION_REWARD, MAX_WALL_COLLISION_REWARD,
                DEFAULT_WALL_COLLISION_REWARD));
        parameterMap.put(LEARNING_RATE_NAME, new Parameter(LEARNING_RATE_NAME,
                ParameterType.DOUBLE, MIN_LEARNING_RATE, MAX_LEARNING_RATE, DEAFULT_LEARNING_RATE));
        parameterMap.put(FUTURE_DISCOUNT_FACTOR_NAME, new Parameter(FUTURE_DISCOUNT_FACTOR_NAME,
                ParameterType.DOUBLE, MIN_FUTURE_DISCOUNT_FACTOR, MAX_FUTURE_DISCOUNT_FACTOR,
                DEFAULT_FUTURE_DISCOUNT_FACTOR));
        parameterMap.put(EXPLORATION_FACTOR_NAME, new Parameter(EXPLORATION_FACTOR_NAME,
                ParameterType.DOUBLE, MIN_EXPLORATION_FACTOR, MAX_EXPLORATION_FACTOR,
                DEFAULT_EXPLORATION_FACTOR));

    }

    @Override
    public Parameter getParameter(String name) {
        if (!parameterMap.containsKey(name)) {
            throw new IllegalArgumentException("Invalid parameter name.");
        }
        Parameter currentParameter = parameterMap.get(name);

        return new Parameter(name, currentParameter.getType(), currentParameter.getMinValue(),
                currentParameter.getMaxValue(), currentParameter.getValue());
    }

    @Override
    public void setParameter(String name, double value) {
        parameterMap.get(name).setValue(value);
    }

    @Override
    public LinkedHashSet<Parameter> getParameters() {
        // LinkedHashSet<Parameter> parameterCopy = new
        // LinkedHashSet<Parameter>();
        // for (Parameter param : parameterMap.values()) {
        // parameterCopy.add(new Parameter(param.getName(), param.getType(),
        // param.getMinValue(),
        // param.getMaxValue(), param.getValue()));
        // }
        // return parameterCopy;
        return new LinkedHashSet<Parameter>(parameterMap.values());
    }

}
