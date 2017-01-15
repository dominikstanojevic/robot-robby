package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions.RewardFunction;
import hr.fer.zemris.projekt.grid.Field;

import org.junit.Assert;
import org.junit.Test;

public class RewardFunctionTest {

    @Test
    public void testBottleCollectSuccesfullCollectOne() {
        State state = State.fromFieldStates(Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY,
                Field.EMPTY);
        Move action = Move.COLLECT;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getPickedBottle()
                - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test
    public void testBottleCollectSuccesfullCollectTwo() {
        State state = State.fromFieldStates(Field.BOTTLE, Field.BOTTLE, Field.BOTTLE, Field.EMPTY,
                Field.EMPTY);
        Move action = Move.COLLECT;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getPickedBottle()
                - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test
    public void testBottleCollectSuccesfullCollectThreeChangedRewardValue() {
        State state = State.fromFieldStates(Field.BOTTLE, Field.BOTTLE, Field.BOTTLE, Field.EMPTY,
                Field.EMPTY);
        Move action = Move.COLLECT;
        ReinforcmentLearningParameters params = new ReinforcmentLearningParameters();
        params.setParameter(ReinforcmentLearningParameters.PICKED_BOTTLE_REWARD_NAME, 50);
        RewardFunction.setRewards(params);
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getPickedBottle()
                - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test
    public void testEmptyPickUpOne() {
        State state = State.fromFieldStates(Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY,
                Field.EMPTY);
        Move action = Move.COLLECT;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getEmptyPickUp()
                - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test
    public void testEmptyPickUpTwo() {
        State state = State.fromFieldStates(Field.EMPTY, Field.BOTTLE, Field.EMPTY, Field.EMPTY,
                Field.EMPTY);
        Move action = Move.COLLECT;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getEmptyPickUp()
                - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test
    public void testEmptyPickUpThree() {
        State state = State.fromFieldStates(Field.EMPTY, Field.BOTTLE, Field.BOTTLE, Field.EMPTY,
                Field.EMPTY);
        Move action = Move.COLLECT;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getEmptyPickUp()
                - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test
    public void testWallCollisionLeft() {
        State state = State.fromFieldStates(Field.EMPTY, Field.WALL, Field.BOTTLE, Field.EMPTY,
                Field.EMPTY);
        Move action = Move.LEFT;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getWallCollision()
                - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test
    public void testWallCollisionRight() {
        State state = State.fromFieldStates(Field.EMPTY, Field.EMPTY, Field.WALL, Field.EMPTY,
                Field.EMPTY);
        Move action = Move.RIGHT;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getWallCollision()
                - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test
    public void testWallCollisionUp() {
        State state = State.fromFieldStates(Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.WALL,
                Field.EMPTY);
        Move action = Move.UP;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getWallCollision()
                - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test
    public void testWallCollisionDown() {
        State state = State.fromFieldStates(Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY,
                Field.WALL);
        Move action = Move.DOWN;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getWallCollision()
                - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void randomMoveReward() {
        State state = State.fromFieldStates(Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY,
                Field.WALL);
        Move action = Move.RANDOM;
        RewardFunction.calculateReward(state, action);
    }

    @Test
    public void skipTurnTestOne() {
        State state = State.fromFieldStates(Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY,
                Field.WALL);
        Move action = Move.SKIP_TURN;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test
    public void skipTurnTestTwo() {
        State state = State.fromFieldStates(Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY,
                Field.WALL);
        Move action = Move.SKIP_TURN;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getLivingReward()) <= 1e-6);
    }

    @Test
    public void skipTurnTestThree() {
        State state = State.fromFieldStates(Field.EMPTY, Field.WALL, Field.WALL, Field.WALL,
                Field.WALL);
        Move action = Move.SKIP_TURN;
        double reward = RewardFunction.calculateReward(state, action);
        Assert.assertTrue(Math.abs(reward - RewardFunction.getLivingReward()) <= 1e-6);
    }
}
