package hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.ReinforcmentLearningParameters;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.State;
import hr.fer.zemris.projekt.grid.Field;

public class RewardFunction {
    // TODO random move reward ??

    private static double livingReward = -0.5;
    private static double pickedBottle = 10;
    private static double emptyPickUp = -2;
    private static double wallCollision = -10;

    public static double getLivingReward() {
        return livingReward;
    }

    public static double getPickedBottle() {
        return pickedBottle;
    }

    public static double getEmptyPickUp() {
        return emptyPickUp;
    }

    public static double getWallCollision() {
        return wallCollision;
    }

    public static void setRewards(ReinforcmentLearningParameters params) {
        livingReward = params.getParameter(ReinforcmentLearningParameters.LIVING_REWARD_NAME)
                .getValue();
        pickedBottle = params
                .getParameter(ReinforcmentLearningParameters.PICKED_BOTTLE_REWARD_NAME).getValue();
        emptyPickUp = params.getParameter(ReinforcmentLearningParameters.EMPTY_PICK_UP_REWARD_NAME)
                .getValue();
        wallCollision = params.getParameter(
                ReinforcmentLearningParameters.WALL_COLLISION_REWARD_NAME).getValue();
    }

    public static double calculateReward(State oldState, Move action) {
        double reward = 0;
        reward += livingReward;
        if (checkBottlePickedUp(oldState, action)) {
            reward += pickedBottle;
        } else if (checkEmptyPickUp(oldState, action)) {
            reward += emptyPickUp;
        } else if (checkWallCollision(oldState, action)) {
            reward += wallCollision;
        }
        return reward;

    }

    private static boolean checkBottlePickedUp(State oldState, Move action) {
        if (oldState.getFieldState(State.StateFields.CURRENT).equals(Field.BOTTLE)
                && action.equals(Move.COLLECT)) {
            return true;
        }
        return false;
    }

    private static boolean checkEmptyPickUp(State oldState, Move action) {
        if (!oldState.getFieldState(State.StateFields.CURRENT).equals(Field.BOTTLE)
                && action.equals(Move.COLLECT)) {
            return true;
        }
        return false;
    }

    private static boolean checkWallCollision(State oldState, Move action) {
        if (oldState.getFieldState(State.StateFields.UP).equals(Field.WALL)
                && action.equals(Move.UP)) {
            return true;
        }
        if (oldState.getFieldState(State.StateFields.DOWN).equals(Field.WALL)
                && action.equals(Move.DOWN)) {
            return true;
        }
        if (oldState.getFieldState(State.StateFields.RIGHT).equals(Field.WALL)
                && action.equals(Move.RIGHT)) {
            return true;
        }
        if (oldState.getFieldState(State.StateFields.LEFT).equals(Field.WALL)
                && action.equals(Move.LEFT)) {
            return true;
        }
        return false;
    }

}
