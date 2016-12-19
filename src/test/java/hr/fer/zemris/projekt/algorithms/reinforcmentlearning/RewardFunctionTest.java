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

}
