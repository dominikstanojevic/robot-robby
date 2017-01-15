package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions.QFunction;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions.RewardFunction;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Assert;
import org.junit.Test;

public class QFunctionTest {
    private static State emptyState = State.fromFieldStates(hr.fer.zemris.projekt.grid.Field.EMPTY,
            hr.fer.zemris.projekt.grid.Field.EMPTY, hr.fer.zemris.projekt.grid.Field.EMPTY,
            hr.fer.zemris.projekt.grid.Field.EMPTY, hr.fer.zemris.projekt.grid.Field.EMPTY);

    @Test
    public void countStateProperCountNoMove() throws NoSuchFieldException, SecurityException,
    IllegalArgumentException, IllegalAccessException {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Field stateActionFieldCount = qfun.getClass().getDeclaredField("stateActionCount");
        stateActionFieldCount.setAccessible(true);
        int count = 0;
        count = countMoves((Map<State, Map<Move, Integer>>) stateActionFieldCount.get(qfun));
        Assert.assertTrue(count == 0);
    }

    @Test
    public void countStateProperCountOneMoveNoRandom() throws NoSuchFieldException,
    SecurityException, IllegalArgumentException, IllegalAccessException {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Field stateActionFieldCount = qfun.getClass().getDeclaredField("stateActionCount");
        stateActionFieldCount.setAccessible(true);
        int count = 0;
        qfun.updateFunction(emptyState, emptyState, Move.DOWN, false);
        count = countMoves((Map<State, Map<Move, Integer>>) stateActionFieldCount.get(qfun));
        Assert.assertTrue(count == 1);
    }

    @Test
    public void countStateProperCountOneMoveRandom() throws NoSuchFieldException,
    SecurityException, IllegalArgumentException, IllegalAccessException {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Field stateActionFieldCount = qfun.getClass().getDeclaredField("stateActionCount");
        stateActionFieldCount.setAccessible(true);
        int count = 0;
        qfun.updateFunction(emptyState, emptyState, Move.DOWN, true);
        count = countMoves((Map<State, Map<Move, Integer>>) stateActionFieldCount.get(qfun));
        Assert.assertTrue(count == 1);
    }

    @Test
    public void countStateProperCountMultipleSameMoveRandom() throws NoSuchFieldException,
    SecurityException, IllegalArgumentException, IllegalAccessException {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Field stateActionFieldCount = qfun.getClass().getDeclaredField("stateActionCount");
        stateActionFieldCount.setAccessible(true);
        int count = 0;
        for (int i = 0; i < 100; i++) {
            qfun.updateFunction(emptyState, emptyState, Move.DOWN, true);
        }
        @SuppressWarnings("unchecked")
        Map<State, Map<Move, Integer>> stateCountMap = (Map<State, Map<Move, Integer>>) stateActionFieldCount
        .get(qfun);
        count = countMoves(stateCountMap);
        Assert.assertTrue(count == 100);
        Assert.assertTrue(stateCountMap.get(emptyState).get(Move.RANDOM).equals(100));
    }

    @Test
    public void countStateProperCountMultipleSameMoveNoRandom() throws NoSuchFieldException,
    SecurityException, IllegalArgumentException, IllegalAccessException {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Field stateActionFieldCount = qfun.getClass().getDeclaredField("stateActionCount");
        stateActionFieldCount.setAccessible(true);
        int count = 0;
        for (int i = 0; i < 100; i++) {
            qfun.updateFunction(emptyState, emptyState, Move.DOWN, false);
        }
        Map<State, Map<Move, Integer>> stateCountMap = (Map<State, Map<Move, Integer>>) stateActionFieldCount
                .get(qfun);
        count = countMoves(stateCountMap);
        Assert.assertTrue(count == 100);
        Assert.assertTrue(stateCountMap.get(emptyState).get(Move.DOWN).equals(100));
    }

    @Test
    public void countStateProperCountMultipleDifferentMoveNoRandom() throws NoSuchFieldException,
    SecurityException, IllegalArgumentException, IllegalAccessException {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Field stateActionFieldCount = qfun.getClass().getDeclaredField("stateActionCount");
        stateActionFieldCount.setAccessible(true);
        int count = 0;
        for (int i = 0; i < 100; i++) {
            for (Move move : Move.values()) {
                if (move.equals(Move.RANDOM)) {
                    qfun.updateFunction(emptyState, emptyState,
                            TestUtility.getRandomMove(ThreadLocalRandom.current()), true);
                } else {
                    qfun.updateFunction(emptyState, emptyState, move, false);
                }

            }
        }
        Map<State, Map<Move, Integer>> stateCountMap = (Map<State, Map<Move, Integer>>) stateActionFieldCount
                .get(qfun);
        count = countMoves(stateCountMap);
        Assert.assertTrue(count == 700);
        Assert.assertTrue(stateCountMap.get(emptyState).get(Move.DOWN).equals(100));
    }

    @Test
    public void rewardIntegrationTestOne() {
        ReinforcmentLearningParameters params = new ReinforcmentLearningParameters();
        params.setParameter(ReinforcmentLearningParameters.EXPLORATION_FACTOR_NAME, 0);
        params.setParameter(ReinforcmentLearningParameters.LEARNING_RATE_NAME, 1);
        params.setParameter(ReinforcmentLearningParameters.FUTURE_DISCOUNT_FACTOR_NAME, 0);
        QFunction qfun = new QFunction(params);
        RewardFunction.setRewards(params);

        qfun.updateFunction(emptyState, emptyState, Move.COLLECT, false);
        double actualValue = qfun.getQValue(emptyState, Move.COLLECT);
        double expectedValue = (params.getParameter(
                ReinforcmentLearningParameters.EMPTY_PICK_UP_REWARD_NAME).getValue() + params
                .getParameter(ReinforcmentLearningParameters.LIVING_REWARD_NAME).getValue())
                * params.getParameter(ReinforcmentLearningParameters.LEARNING_RATE_NAME).getValue();
        Assert.assertTrue("Empty pickup reward test failed expected:" + expectedValue
                + ", actual: " + actualValue, Math.abs(actualValue - expectedValue) < 1e-6);
    }

    @Test
    public void getMoveInitializeValuesEmptyState() {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Assert.assertTrue(qfun.getQValue(emptyState, Move.COLLECT) == 0);
        Assert.assertTrue(qfun.getQValue(emptyState, Move.DOWN) == 0);
        Assert.assertTrue(qfun.getQValue(emptyState, Move.LEFT) == 0);
        Assert.assertTrue(qfun.getQValue(emptyState, Move.RANDOM) == 0);
        Assert.assertTrue(qfun.getQValue(emptyState, Move.RIGHT) == 0);
        Assert.assertTrue(qfun.getQValue(emptyState, Move.SKIP_TURN) == 0);
        Assert.assertTrue(qfun.getQValue(emptyState, Move.UP) == 0);
    }

    @Test
    public void getMoveInitializeValuesEmptyStateCollect() {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Assert.assertTrue(qfun.getQValue(emptyState, Move.COLLECT) == 0);

    }

    @Test
    public void getMoveInitializeValuesEmptyStateDown() {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Assert.assertTrue(qfun.getQValue(emptyState, Move.DOWN) == 0);
    }

    @Test
    public void getMoveInitializeValuesEmptyStateLeft() {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Assert.assertTrue(qfun.getQValue(emptyState, Move.LEFT) == 0);
    }

    @Test
    public void getMoveInitializeValuesEmptyStateRandom() {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Assert.assertTrue(qfun.getQValue(emptyState, Move.RANDOM) == 0);
    }

    @Test
    public void getMoveInitializeValuesEmptyStateRight() {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Assert.assertTrue(qfun.getQValue(emptyState, Move.RIGHT) == 0);
    }

    @Test
    public void getMoveInitializeValuesEmptyStateSkipTurn() {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Assert.assertTrue(qfun.getQValue(emptyState, Move.SKIP_TURN) == 0);
    }

    @Test
    public void getMoveInitializeValuesEmptyStateUp() {
        QFunction qfun = new QFunction(new ReinforcmentLearningParameters());
        Assert.assertTrue(qfun.getQValue(emptyState, Move.UP) == 0);
    }

    private static int countMoves(Map<State, Map<Move, Integer>> stateActionCount) {
        int count = 0;
        for (State s : stateActionCount.keySet()) {
            Map<Move, Integer> currCountMap = stateActionCount.get(s);
            for (Move m : currCountMap.keySet()) {
                count += currCountMap.get(m);
            }
        }
        return count;

    }
}
