package hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.ReinforcmentLearningParameters;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that models q learning function.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.1.2017.
 */
public class QFunction {

    /**
     * Map of q values for state action pairs.
     */
    private Map<State, Map<Move, Double>> qValues;
    /**
     * Frequency of occurrence of state action pairs.
     */
    private Map<State, Map<Move, Integer>> stateActionCount;

    /**
     * Learning rate.
     */
    private double learningRate;
    /**
     * New experience discount factor.
     */
    private double discountFactor;
    /**
     * Exploration factor. Determines how much to prefer new state action pair
     * over prize.
     */
    private double explorationFactor;

    /**
     * Initial q value.
     */
    private final static double initialQValue = 0;
    /**
     * Initial state count value.
     */
    private final static int initialCountValue = 0;
    /**
     * Initial optimal future reward value.
     */
    private final static double initialOptimalFutureReward = 0;

    public QFunction(Map<State, Map<Move, Double>> qValues,
            Map<State, Map<Move, Integer>> stateActionCount, ReinforcmentLearningParameters params) {
        this(params);
        this.qValues = qValues;
        this.stateActionCount = stateActionCount;

    }

    /**
     * Method initializes q function with given qfunction and current
     * reinforcment learining parameters.
     *
     * @param qFunction
     *            qfunction
     * @param params
     *            learning parameters
     */
    public QFunction(QFunction qFunction, ReinforcmentLearningParameters params) {
        this(params);
        qValues = qFunction.qValues;
        stateActionCount = qFunction.stateActionCount;
    }

    public QFunction(ReinforcmentLearningParameters params) {
        qValues = new HashMap<State, Map<Move, Double>>();
        stateActionCount = new HashMap<State, Map<Move, Integer>>();
        learningRate = params.getParameter(ReinforcmentLearningParameters.LEARNING_RATE_NAME)
                .getValue();
        discountFactor = params.getParameter(
                ReinforcmentLearningParameters.FUTURE_DISCOUNT_FACTOR_NAME).getValue();
        explorationFactor = params.getParameter(
                ReinforcmentLearningParameters.EXPLORATION_FACTOR_NAME).getValue();
    }

    public double getQValue(State state, Move action) {
        if (!qValues.containsKey(state)) {
            initializeQValue(state);
        }

        return qValues.get(state).get(action);
    }

    /**
     * Method sets q value of given state action pair to given value.
     *
     * @param state
     *            state for which to set q value
     * @param action
     *            action in the state for which to set q value
     * @param newValue
     *            new q value for action state pair
     */
    private void setQValue(State state, Move action, double newValue) {
        if (!qValues.containsKey(state)) {
            initializeQValue(state);
        }
        qValues.get(state).put(action, newValue);
    }

    /**
     * Method initializes q values for given state.
     *
     * @param state
     *            state for which to initialize q value
     */
    private void initializeQValue(State state) {
        Map<Move, Double> qValueMap = new HashMap<>();
        for (Move move : Move.values()) {
            qValueMap.put(move, initialQValue);
        }
        qValues.put(state, qValueMap);

    }

    public void updateFunction(State oldState, State currState, Move action, boolean randomAction) {
        if (oldState == null || action == null) {
            return;
        }

        if (randomAction == true) {
            updateStateActionCount(oldState, Move.RANDOM);
        } else {
            updateStateActionCount(oldState, action);
        }

        updateQValue(oldState, currState, action, randomAction);
    }

    private void updateQValue(State oldState, State currState, Move action, boolean randomAction) {

        double reward = RewardFunction.calculateReward(oldState, action);
        if (randomAction == true) {
            action = Move.RANDOM;
        }
        double currentQValue = getQValue(oldState, action);
        double optimalFutureReward = calcOptimalFutureValue(currState);
        double newValue = (1 - learningRate) * currentQValue + learningRate
                * (reward + discountFactor * optimalFutureReward);

        setQValue(oldState, action, newValue);
    }

    private void initializeStateActionCount(State state) {

        Map<Move, Integer> actionCount = new HashMap<>();
        for (Move move : Move.values()) {
            actionCount.put(move, initialCountValue);
        }
        stateActionCount.put(state, actionCount);

    }

    private void updateStateActionCount(State state, Move action) {
        if (!stateActionCount.containsKey(state)) {
            initializeStateActionCount(state);
        }
        int currentCount = stateActionCount.get(state).get(action);
        stateActionCount.get(state).put(action, currentCount + 1);
    }

    private int getStateActionCount(State state, Move action) {
        if (!stateActionCount.containsKey(state)) {
            initializeStateActionCount(state);
        }
        return stateActionCount.get(state).get(action);
    }

    private double calcOptimalFutureValue(State currState) {
        // TODO provjeri da li je uredu tu pridruživati inicijalnu optimalnu
        // nagradu
        double optimalFutureReward = initialOptimalFutureReward;
        for (Move action : Move.values()) {
            int stateActionCount = getStateActionCount(currState, action);
            double currentQValue = getQValue(currState, action);
            double currentEstimation = calculateExplorationValue(currentQValue, stateActionCount);
            if (currentEstimation > optimalFutureReward) {
                optimalFutureReward = currentEstimation;
            }
        }
        return optimalFutureReward;

    }

    private double calculateExplorationValue(double valueEstimate, int visitCount) {
        double explorationValue = valueEstimate + explorationFactor / (visitCount + 1);
        return explorationValue;
    }

    /**
     * Method obtains optimal move according to q function for current state.
     *
     * @param currState
     *            current state
     * @return calculated move
     */
    public Move getMove(State currState) {

        if (!qValues.containsKey(currState)) {
            initializeQValue(currState);
        }

        Map.Entry<Move, Double> maxMoveEntry = qValues.get(currState).entrySet().stream()
                .max((x, y) -> x.getValue().compareTo(y.getValue())).get();

        return maxMoveEntry.getKey();

    }

    /**
     * MEthod initializes QFunction from string.
     *
     * @param description
     *            qfunction description
     * @param params
     *            reinforcement learning parameters contex
     * @return initialized qfunction
     */
    public static QFunction fromString(String description, ReinforcmentLearningParameters params) {
        int qValuesStartIndex = description.indexOf("Q");
        int qValuesEndIndex = description.indexOf("N{", qValuesStartIndex) - 1;
        String qValuesString = description.substring(qValuesStartIndex + 1, qValuesEndIndex).trim();
        Map<State, Map<Move, Double>> qValues = extractValues(qValuesString.substring(1,
                qValuesString.length() - 1));

        int actionStateCountStartIndex = description.indexOf("N{");
        int actionStateCountEndIndex = description.lastIndexOf("}");
        Map<State, Map<Move, Integer>> stateActionCount = extractValues(description.substring(
                actionStateCountStartIndex + 2, actionStateCountEndIndex));

        return new QFunction(qValues, stateActionCount, params);
    }

    private static <T extends Number> Map<State, Map<Move, T>> extractValues(String valuesString) {
        Map<State, Map<Move, T>> values = new HashMap<>();

        int currIndex = 0;
        int delimiterIndex = valuesString.indexOf(":");
        int innerMapStartIndex = valuesString.indexOf("{", delimiterIndex);
        int innerMapEndIndex = valuesString.indexOf("}", innerMapStartIndex);
        while (currIndex < valuesString.length() && delimiterIndex != -1
                && innerMapStartIndex != -1 && innerMapEndIndex != -1) {

            State currState = State.fromString(valuesString.substring(currIndex, delimiterIndex)
                    .trim());
            Map<Move, T> innerMap = extractInnerValueMap(valuesString.substring(
                    innerMapStartIndex + 1, innerMapEndIndex).trim());

            values.put(currState, innerMap);

            currIndex = valuesString.indexOf(",", innerMapEndIndex);
            if (currIndex == -1) {
                break;
            } else {
                currIndex++;
            }
            delimiterIndex = valuesString.indexOf(":", currIndex + 1);
            innerMapStartIndex = valuesString.indexOf("{", delimiterIndex);
            innerMapEndIndex = valuesString.indexOf("}", innerMapStartIndex);

        }

        return values;
    }

    /**
     * Method extracts map from a map in string q function description. It is
     * used for q values where it maps move to q value and for state action
     * count where it maps move to count.
     *
     * @param mapString
     *            inner map string
     * @return mapper which maps move to value
     */
    private static <T extends Number> Map<Move, T> extractInnerValueMap(String mapString) {
        String[] entries = mapString.split(",");
        Map<Move, T> innerMap = new HashMap<>();

        for (String entry : entries) {
            int delimiterIndex = entry.indexOf(":");

            Move currMove = extractMoveFromString(entry.substring(0, delimiterIndex).trim());
            @SuppressWarnings("unchecked")
            T currValue = (T) extractValueFromString(entry.substring(delimiterIndex + 1).trim());
            innerMap.put(currMove, currValue);
        }

        return innerMap;

    }

    /**
     * Method extracts move from given string. It expects string representation
     * of the Move described in {@link Move}. String needs to be trimed before
     * calling this function.
     *
     * @param moveString
     *            string containing a move
     * @return extracted move
     * @throws IllegalArgumentException
     *             if given string doesn't match legal move
     */
    private static Move extractMoveFromString(String moveString) {
        for (Move move : Move.values()) {
            if (moveString.equals(move.toString())) {
                return move;
            }
        }
        throw new IllegalArgumentException("Illegal move string");
    }

    /**
     * Method extracts number value from string. If the number contains "." it
     * returns a double, otherwise it returns integer.
     *
     * @param valueString
     *            trimed number value string
     * @return number representation of the string
     * @throws NumberFormatException
     *             if the given string has illegal number format
     */
    private static Number extractValueFromString(String valueString) {
        if (valueString.contains(".")) {
            return Double.parseDouble(valueString);
        } else {
            return Integer.parseInt(valueString);
        }

    }

    @Override
    public String toString() {
        String qValuesString = "Q{";
        List<String> qValuesStringParts = new ArrayList<>();
        for (State state : qValues.keySet()) {

            String qValuesStringPart = state.toString() + ": {";
            List<String> actionValueParts = new ArrayList<>();
            if (qValues.get(state) != null) {
                for (Move action : qValues.get(state).keySet()) {

                    actionValueParts.add(action + ":" + qValues.get(state).get(action));
                }
                qValuesStringPart += String.join(", ", actionValueParts);
            }
            qValuesStringPart += "}";
            qValuesStringParts.add(qValuesStringPart);
        }
        qValuesString += String.join(", \n", qValuesStringParts);
        qValuesString += "}";

        String countValuesString = "N{";
        List<String> countValuesStringParts = new ArrayList<>();
        for (State state : stateActionCount.keySet()) {

            String countValuesStringPart = state.toString() + ": {";
            List<String> actionCountParts = new ArrayList<>();
            if (stateActionCount.get(state) != null) {
                for (Move action : stateActionCount.get(state).keySet()) {

                    actionCountParts.add(action + ":" + stateActionCount.get(state).get(action));
                }
                countValuesStringPart += String.join(", ", actionCountParts);
            }
            countValuesStringPart += "}";
            countValuesStringParts.add(countValuesStringPart);
        }
        countValuesString += String.join(", \n", countValuesStringParts);
        countValuesString += "}";

        return qValuesString + "\n" + countValuesString;
    }
}
