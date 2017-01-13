package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions.QFunction;

import java.lang.reflect.Field;
import java.util.Map;

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
                    qfun.updateFunction(emptyState, emptyState, move, true);
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
