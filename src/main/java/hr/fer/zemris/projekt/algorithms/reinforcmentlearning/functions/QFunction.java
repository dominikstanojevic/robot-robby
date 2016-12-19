package hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.ReinforcmentLearningParameters;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QFunction {

    private Map<State, Map<Move, Double>> qValues;
    private Map<State, Map<Move, Integer>> stateActionCount;

    private double learningRate;
    private double discountFactor;
    private double explorationFactor;

    private final static double initialQValue = 0;
    private final static int initialCountValue = 0;
    private final static double initialOptimalFutureReward = 0;

    public QFunction(Map<State, Map<Move, Double>> qValues,
            Map<State, Map<Move, Integer>> stateActionCount, ReinforcmentLearningParameters params) {
        this(params);
        this.qValues = qValues;
        this.stateActionCount = stateActionCount;

    }

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

    private double getQValue(State state, Move action) {
        if (!qValues.containsKey(state)) {
            initializeQValue(state);
        }
        return qValues.get(state).get(action);
    }

    private void setQValue(State state, Move action, double newValue) {
        if (!qValues.containsKey(state)) {
            initializeQValue(state);
        }
        qValues.get(state).put(action, newValue);
    }

    private void initializeQValue(State state) {
        Map<Move, Double> qValueMap = new HashMap<>();
        for (Move move : Move.values()) {
            qValueMap.put(move, initialQValue);
        }
        qValues.put(state, qValueMap);

    }

    public void updateFunction(State oldState, State currState, Move action) {
        if (oldState == null || action == null) {
            return;
        }
        updateStateActionCount(oldState, action);
        updateQValue(oldState, currState, action);
    }

    private void updateQValue(State oldState, State currState, Move action) {
        double currentQValue = getQValue(oldState, action);

        double reward = RewardFunction.calculateReward(oldState, action);

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

    public Move getMove(State currState) {

        if (!qValues.containsKey(currState)) {
            initializeQValue(currState);
        }

        Map.Entry<Move, Double> maxMoveEntry = qValues.get(currState).entrySet().stream()
                .max((x, y) -> x.getValue().compareTo(y.getValue())).get();

        return maxMoveEntry.getKey();

    }

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

    private static <T extends Number> Map<Move, T> extractInnerValueMap(String mapString) {
        String[] entries = mapString.split(",");
        Map<Move, T> innerMap = new HashMap<>();

        for (String entry : entries) {
            int delimiterIndex = entry.indexOf(":");

            Move currMove = extractMoveFromString(entry.substring(0, delimiterIndex).trim());
            T currValue = (T) extractValueFromString(entry.substring(delimiterIndex + 1).trim());
            innerMap.put(currMove, currValue);
        }

        return innerMap;

    }

    private static Move extractMoveFromString(String moveString) {
        for (Move move : Move.values()) {
            if (moveString.equals(move.toString())) {
                return move;
            }
        }
        throw new IllegalArgumentException("Illegal move string");
    }

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
