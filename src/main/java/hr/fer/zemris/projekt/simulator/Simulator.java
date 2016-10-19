package hr.fer.zemris.projekt.simulator;

import hr.fer.zemris.projekt.algorithms.Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A single threaded game simulator that is able to take a reference to an
 * algorithm and use it to run it on multiple grids in order to generate
 * statistics of the game.
 *
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class Simulator extends AbstractSimulator {
    /**
     * Creates a new {@link Simulator} with the maximal number of moves
     * equal to the ones given in the argument.
     *
     * @param maxMoves the maximal number of moves.
     */
    public Simulator(int maxMoves) {
        super(maxMoves);
    }

    /**
     * Creates a new {@link Simulator} with the default number of moves.
     */
    public Simulator() {
        super();
    }

    @Override
    public List<Stats> playGames(Algorithm robot) {
        {
            if (grids == null){
                throw new IllegalStateException("There are no defined grids for this simulation.");
            }
            Random rnd = new Random();

            List<Stats> stats = new ArrayList<>();

            for (int i = 0; i < grids.length; ++i){
                stats.add(playGame(robot, grids[i], rnd));
            }

            return stats;
        }
    }
}
