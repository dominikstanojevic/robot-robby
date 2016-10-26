package hr.fer.zemris.projekt.simulator;

import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.grid.IGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A single threaded game simulator that is able to take a reference to an
 * algorithm and use it to run it on multiple grids in order to generate
 * statistics of the game.
 *
 * @author Kristijan Vulinovic
 * @version 1.0.1
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
    public List<Stats> playGames(Robot robot) {
        {
            if (grids == null){
                throw new IllegalStateException("There are no defined grids for this simulation.");
            }
            Random rnd = new Random();

            List<Stats> stats = new ArrayList<>();

            for (IGrid grid : grids) {
                stats.add(playGame(robot, grid, rnd));
            }

            return stats;
        }
    }
}
