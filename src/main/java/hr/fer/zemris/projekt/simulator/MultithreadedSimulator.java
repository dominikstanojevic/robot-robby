package hr.fer.zemris.projekt.simulator;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.grid.IGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * A multi threaded game simulator that is able to take a reference to an
 * algorithm and use it to run it on multiple grids in order to generate
 * statistics of the game. Every game will be run on it's own thread, but
 * there is no guarantee on the order of the games played nor the order of
 * algorithm nexMove calls.
 *
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class MultithreadedSimulator extends AbstractSimulator {
    private ExecutorService pool;

    /**
     * Creates a new {@link MultithreadedSimulator} with the maximal number
     * of moves equal to the one given in the argument. Creates a thread
     * pool that will be used for game playing.
     *
     * @param maxMoves the maximal number of moves.
     */
    public MultithreadedSimulator(int maxMoves) {
        super(maxMoves);

        int numberOfCores = Runtime.getRuntime().availableProcessors();
        pool = Executors.newFixedThreadPool(numberOfCores, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
    }

    /**
     * Creates a new {@link MultithreadedSimulator} with the default maximal
     * number of moves equal to the one given in the argument. Creates a thread
     * pool that will be used for game playing.
     */
    public MultithreadedSimulator() {
        this(AbstractSimulator.DEFAULT_MAX_MOVES);
    }

    @Override
    public List<Stats> playGames(Algorithm robot) {
        if (grids == null){
            throw new IllegalStateException("There are no defined grids for this simulation.");
        }
        Random rnd = new Random();
        List<Future<Stats>> results = new ArrayList<>();
        for (int i = 0; i < grids.length; ++i){
            PlayGame job = new PlayGame(robot, grids[i], rnd);
            results.add(pool.submit(job));
        }

        List<Stats> stats = new ArrayList<>();
        for (Future<Stats> job : results) {
            try {
                Stats stat = job.get();
                stats.add(stat);
            } catch (InterruptedException | ExecutionException e) {}
        }

        return stats;
    }

    /**
     * A {@link Callable} class that plays a single game on a single thread.
     * The result of the game will be given as a stats class.
     *
     * @author Kristijan Vulinovic
     * @version 1.0.0
     */
    private class PlayGame implements Callable<Stats>{
        /**
         * The algorithm that should be used to play the game.
         */
        private Algorithm robot;
        /**
         * The grid that should be used to play the game on.
         */
        private IGrid grid;
        /**
         * Random number generator used to get a random move.
         */
        private Random rnd;

        /**
         * Creates a new {@link PlayGame} with the given robot and grid.
         *
         * @param robot The algorithm that should be used to play the game.
         * @param grid The grid that should be used to play the game on.
         * @param rnd Random number generator used to get a random move.
         */
        public PlayGame(Algorithm robot, IGrid grid, Random rnd){
            this.robot = robot;
            this.grid = grid;
            this.rnd = rnd;
        }

        @Override
        public Stats call() throws Exception {
            return playGame(robot, grid, rnd);
        }
    }
}
