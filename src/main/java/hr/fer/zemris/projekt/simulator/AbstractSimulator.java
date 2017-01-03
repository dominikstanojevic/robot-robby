package hr.fer.zemris.projekt.simulator;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.neural.Utils;
import hr.fer.zemris.projekt.algorithms.neural.elman.ElmanNeuralNetwork;
import hr.fer.zemris.projekt.grid.Field;
import hr.fer.zemris.projekt.grid.Grid;
import hr.fer.zemris.projekt.grid.IGrid;
import hr.fer.zemris.projekt.observer.Observable;
import hr.fer.zemris.projekt.observer.Observer;
import hr.fer.zemris.projekt.observer.observations.RobotActionTaken;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>Provides the functionality to run multiple simulations on a single bot and
 * return the result statistics. The simulations can be run multi threaded or
 * single threaded, depending on the implementation. The simulator ensures to
 * use the same set of grids for simulations in every call, unless the grid
 * list is changed manually.</p>
 * <p>
 * <p>This simulator also represents the subject in the observer design pattern.
 * Observers can be notified whenever a move is made.</p>
 *
 * @author Kristijan Vulinovic, Leon Luttenberger
 * @version 1.1.4
 */
public abstract class AbstractSimulator implements Observable<RobotActionTaken> {

    /**
     * The default maximal number of moves.
     */
    public static final int DEFAULT_MAX_MOVES = 200;

    /**
     * An array of grids used to play the games.
     */
    protected IGrid[] grids;

    /**
     * The maximal number of moves allowed in a single game.
     */
    protected int maxMoves;

    /**
     * Creates a new AbstractSimulator with the given maximal number of moves.
     *
     * @param maxMoves the maximal number of moves for a single game.
     */
    public AbstractSimulator(int maxMoves) {
        if (maxMoves < 0) {
            throw new IllegalArgumentException("Maximum number of moves has to be a positive number!");
        }

        this.maxMoves = maxMoves;
    }

    /**
     * Creates a new AbstractSimulator with the default number of maximal moves.
     */
    public AbstractSimulator() {
        this(DEFAULT_MAX_MOVES);
    }

    /**
     * Plays games on every defined grid.
     *
     * @param robot the {@link Robot} being tested
     * @return a List of {@link Stats} describing every game played.
     */
    public abstract List<Stats> playGames(Robot robot);

    /**
     * Generates the given amount of random new grids. The parameters specify
     * the number of bottles in the grid, the height and width. It is also
     * possible to enable walls inside of the grid.
     *
     * @param numberOfGrids   the number of grids to be generated.
     * @param numberOfBottles the number of bottles in the grids.
     * @param width           the width of the grids.
     * @param height          the height of the grids.
     * @param hasWalls        a boolean flag indicating if the grid can
     *                        have walls inside it or not.
     */
    public void generateGrids(int numberOfGrids, int numberOfBottles, int width, int height, boolean hasWalls) {
        grids = new Grid[numberOfGrids];

        for (int i = 0; i < numberOfGrids; ++i) {
            grids[i] = new Grid();
            grids[i].generate(width, height, numberOfBottles, hasWalls);
        }
    }

    public void generateGrids(int numberOfGirds, int width, int height, boolean hasWalls) {
        grids = new Grid[numberOfGirds];

        for (int i = 0; i < numberOfGirds; i++) {
            grids[i] = new Grid();

            int numberOfBottles = (int) Utils.RANDOM.nextGaussian() * 15 + 50;
            numberOfBottles = Math.max(1, Math.min(numberOfBottles, 100));

            grids[i].generate(width, height, numberOfBottles, hasWalls);
        }
    }

    /**
     * Generates the given amount of random new grids. Number of bottles are distributed using normal distribution.
     * The parameters specify grid height and width. It is also possible to enable walls inside of the grid.
     * @param numberOfGirds the number of grids to be generated
     * @param width the width of the grids
     * @param height the height of the grids
     * @param hasWalls a boolean flag indicating if the grid can have walls inside or not
     * @param random random used for calculating the number of the bottles
     */
    public void generateGrids(int numberOfGirds, int width, int height, boolean hasWalls, Random random) {
        grids = new Grid[numberOfGirds];

        for (int i = 0; i < numberOfGirds; i++) {
            grids[i] = new Grid();

            int numberOfBottles = (int) random.nextGaussian() * 15 + 50;
            numberOfBottles = Math.max(1, Math.min(numberOfBottles, 100));

            grids[i].generate(width, height, numberOfBottles, hasWalls);
        }
    }

    /**
     * Reads the files in the list in order to create all the grids
     * defined by the files.
     *
     * @param filePaths list of file paths containing the grid definitions.
     * @throws IOException if an I/O error occurs when reading from one
     *                     of the files
     */
    public void readGridFromFile(List<Path> filePaths) throws IOException {
        int n = filePaths.size();
        grids = new Grid[n];

        for (int i = 0; i < n; ++i) {
            grids[i] = new Grid();
            grids[i].readFromFile(filePaths.get(i));
        }
    }

    /**
     * Sets the grid used for simulations to the one given in the argument.
     *
     * @param grid the grid that should be used.
     */
    public void setGrid(IGrid grid) {
        grids = new Grid[1];
        grids[0] = grid;
    }

    /**
     * Returns a random move from the following ones: {@link Move#UP},
     * {@link Move#DOWN}, {@link Move#LEFT}, {@link Move#RIGHT}.
     *
     * @param rnd a random number generator that is used to
     *            get a random move.
     * @return the random generated {@link Move}.
     */
    private static Move getRandomMove(Random rnd) {
        Move nextMove = null;

        int moveID = rnd.nextInt(4);
        switch (moveID) {
            case 0:
                nextMove = Move.UP;
                break;
            case 1:
                nextMove = Move.DOWN;
                break;
            case 2:
                nextMove = Move.LEFT;
                break;
            case 3:
                nextMove = Move.RIGHT;
                break;
        }

        return nextMove;
    }

    /**
     * Calculates the next move for the given {@link Robot} on the given
     * {@link IGrid}, from the current column and row.
     *
     * @param robot  the {@link Robot} who's being asked for his next move
     * @param grid   the current grid.
     * @param row    the current row.
     * @param column the current column.
     * @return the {@link Move} that the robot should make.
     */
    private Move getNextMove(Robot robot, IGrid grid, int row, int column) {
        Field current = grid.getField(row, column);
        Field left = grid.getField(row, column - 1);
        Field right = grid.getField(row, column + 1);
        Field up = grid.getField(row - 1, column);
        Field down = grid.getField(row + 1, column);

        return robot.nextMove(current, left, right, up, down);
    }

    /**
     * Plays one game on the given grid. The game is executed without interrupting.
     *
     * @param robot        the {@link Robot} being tested
     * @param originalGrid the {@link IGrid} that should be used to play the game.
     * @param rnd          a random number generator, used to play a random move.
     * @return a {@link Stats} object describing every detail about the game.
     */
    protected Stats playGame(Robot robot, IGrid originalGrid, Random rnd) {
        if (robot instanceof ElmanNeuralNetwork){
            ((ElmanNeuralNetwork) robot).clearContext();
        }
        IGrid grid = originalGrid.copy();

        int moveNumber = 0;
        int wallsHit = 0;
        int emptyPickups = 0;
        List<Move> moves = new ArrayList<>();

        int x = grid.getCurrentRow();
        int y = grid.getCurrentColumn();

        while (moveNumber < maxMoves && grid.hasBottlesLeft()) {
            moveNumber++;

            Move nextMove = getNextMove(robot, grid, x, y);
            moves.add(nextMove);

            int xMove = 0;
            int yMove = 0;
            if (nextMove == Move.RANDOM) {
                nextMove = getRandomMove(rnd);
            }
            switch (nextMove) {
                case UP:
                    xMove = -1;
                    break;
                case DOWN:
                    xMove = 1;
                    break;
                case LEFT:
                    yMove = -1;
                    break;
                case RIGHT:
                    yMove = 1;
                    break;
                case SKIP_TURN:
                    break;
                case COLLECT:
                    if (grid.getField(x, y) == Field.BOTTLE) {
                        grid.setField(x, y, Field.EMPTY);
                    } else {
                        emptyPickups++;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("The given move is not supported!");
            }

            int newX = x + xMove;
            int newY = y + yMove;

            // notify listeners, if there are any
            notifyListeners(originalGrid, nextMove, x, y, newX, newY);

            if (grid.getField(newX, newY) == Field.WALL) {
                wallsHit++;
            } else {
                x = newX;
                y = newY;
            }
        }

        int bottlesLeft = grid.getNumberOfBottles();
        int bottlesCollected = originalGrid.getNumberOfBottles() - bottlesLeft;

        return new Stats(moveNumber, bottlesCollected, bottlesLeft, wallsHit, emptyPickups, originalGrid, moves);
    }

    /**
     * List of observers.
     */
    private List<Observer<RobotActionTaken>> observers;

    /**
     * Notifies the listeners with a {@link RobotActionTaken} object only if somebody is observing this
     * object.
     *
     * @param grid grid that the move was taken on
     * @param move move taken
     * @param oldX previous X coordinate of the robot
     * @param oldY previous Y coordinate of the robot
     * @param newX current X coordinate of the robot
     * @param newY current Y coordinate of the robot
     */
    private void notifyListeners(IGrid grid, Move move, int oldX, int oldY, int newX, int newY) {
        if (observers == null || observers.isEmpty()) {
            return;
        }

        this.fire(new RobotActionTaken(grid, move, oldX, oldY, newX, newY));
    }

    @Override
    public void addObserver(Observer<RobotActionTaken> observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }

        observers = new ArrayList<>(observers);
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<RobotActionTaken> observer) {
        if (observers != null) {
            observers = new ArrayList<>();
            observers.remove(observer);
        }
    }

    @Override
    public void fire(RobotActionTaken observation) {
        if (observers != null) {
            for (Observer<RobotActionTaken> observer : observers) {
                observer.observationMade(this, observation);
            }
        }
    }
}
