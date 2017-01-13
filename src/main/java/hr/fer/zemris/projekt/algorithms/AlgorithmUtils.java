package hr.fer.zemris.projekt.algorithms;

import hr.fer.zemris.projekt.simulator.Stats;

import java.util.List;

/**
 * Utility class for {@link Algorithm algorithm} implementations.
 *
 * @author Leon Luttenberger
 */
public final class AlgorithmUtils {

    /** Number of points rewarded for each collected bottle. */
    private static final double BOTTLE_COLLECTED_REWARD = 10;

    /** Number of points reduced for each empty pickup. */
    private static final double EMPTY_PICKUP_PENALTY = 1;

    /** Number of  points reduced for each wall hit. */
    private static final double WALL_HIT_PENALTY = 5;

    /**
     * Calculates the fitness of an individual based on the {@link Stats} for each
     * cleaning sessions.
     * @param stats list of {@link Stats} for each cleaning session
     * @return fitness of the individual in question
     */
    public static double calculateFitness(List<Stats> stats) {
        if (stats.size() == 0) {
            throw new IllegalArgumentException("Stats list is empty.");
        }

        double fitness = 0;

        for (Stats stat : stats) {
            double max = (stat.getBottlesCollected() + stat.getBottlesLeft()) * BOTTLE_COLLECTED_REWARD;

            fitness += stat.getBottlesCollected() * BOTTLE_COLLECTED_REWARD / max;
            fitness -= stat.getEmptyPickups() * EMPTY_PICKUP_PENALTY / max;
            fitness -= stat.getWallsHit() * WALL_HIT_PENALTY / max;
        }

        return fitness / stats.size();
    }

    /**
     * Private constructor to prevent instantiating this utility class.
     */
    private AlgorithmUtils() {}
}
