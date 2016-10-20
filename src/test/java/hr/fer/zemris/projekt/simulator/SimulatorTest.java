package hr.fer.zemris.projekt.simulator;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.grid.Field;
import hr.fer.zemris.projekt.grid.Grid;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("javadoc")
public class SimulatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testSimulatorConstructorWithNegativeMoves() {
        new Simulator(-2);
    }

    // 2 pickups and 1 empty pickups, along with moving down and left
    private Stats playSimulatorGame1() {
        Grid grid = new Grid();

        Field[][] fields = new Field[][] {
                {Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY},
                {Field.EMPTY, Field.EMPTY, Field.BOTTLE /*start*/, Field.EMPTY},
                {Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY},
                {Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.BOTTLE}
        };
        grid.setGrid(fields, 1, 2);

        Algorithm algorithm = mock(Algorithm.class);

        //collect
        when(algorithm.nextMove(Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY)).thenReturn(Move.COLLECT);

        //move left
        when(algorithm.nextMove(Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY)).thenReturn(Move.LEFT);

        //move down
        when(algorithm.nextMove(Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.BOTTLE)).thenReturn(Move.DOWN);

        //collect
        when(algorithm.nextMove(Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY)).thenReturn(Move.COLLECT);

        //do an empty pickup
        when(algorithm.nextMove(Field.EMPTY, Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY)).thenReturn(Move.COLLECT);

        Simulator simulator = new Simulator(5);
        return simulator.playGame(algorithm, grid, new Random());
    }

    @Test
    public void testSimulatorGame1BottlesCount() {
        Stats stats = playSimulatorGame1();
        assertEquals(2, stats.getBottlesCollected());
    }

    @Test
    public void testSimulatorGame1BottlesLeft() {
        Stats stats = playSimulatorGame1();
        assertEquals(3, stats.getBottlesLeft());
    }

    @Test
    public void testSimulatorGame1EmptyPickups() {
        Stats stats = playSimulatorGame1();
        assertEquals(1, stats.getEmptyPickups());
    }

    @Test
    public void testSimulatorGame1MovesNeeded() {
        Stats stats = playSimulatorGame1();
        assertEquals(5, stats.getMovesNeeded());
    }

    @Test
    public void testSimulatorGame1WallsHit() {
        Stats stats = playSimulatorGame1();
        assertEquals(0, stats.getWallsHit());
    }

    // 1 collection, hits a wall and moves right and up
    private Stats playSimulatorGame2() {
        Grid grid = new Grid();

        Field[][] fields = new Field[][] {
                {Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY},
                {Field.EMPTY, Field.EMPTY, Field.BOTTLE, Field.EMPTY},
                {Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY},
                {Field.EMPTY, Field.EMPTY, Field.EMPTY/*start*/, Field.BOTTLE}
        };
        grid.setGrid(fields, 3, 2);

        Algorithm algorithm = mock(Algorithm.class);

        //move right
        when(algorithm.nextMove(Field.EMPTY, Field.EMPTY, Field.BOTTLE, Field.EMPTY, Field.WALL)).thenReturn(Move.RIGHT);

        //collect
        when(algorithm.nextMove(Field.BOTTLE, Field.EMPTY, Field.WALL, Field.EMPTY, Field.WALL)).thenReturn(Move.COLLECT);

        //move up
        when(algorithm.nextMove(Field.EMPTY, Field.EMPTY, Field.WALL, Field.EMPTY, Field.WALL)).thenReturn(Move.UP);

        //move right
        when(algorithm.nextMove(Field.EMPTY, Field.EMPTY, Field.WALL, Field.EMPTY, Field.EMPTY)).thenReturn(Move.RIGHT);

        Simulator simulator = new Simulator(5);
        return simulator.playGame(algorithm, grid, new Random());
    }

    @Test
    public void testSimulatorGame2BottlesCount() {
        Stats stats = playSimulatorGame2();
        assertEquals(1, stats.getBottlesCollected());
    }

    @Test
    public void testSimulatorGame2BottlesLeft() {
        Stats stats = playSimulatorGame2();
        assertEquals(4, stats.getBottlesLeft());
    }

    @Test
    public void testSimulatorGame2EmptyPickups() {
        Stats stats = playSimulatorGame2();
        assertEquals(0, stats.getEmptyPickups());
    }

    @Test
    public void testSimulatorGame2MovesNeeded() {
        Stats stats = playSimulatorGame2();
        assertEquals(5, stats.getMovesNeeded());
    }

    @Test
    public void testSimulatorGame2WallsHit() {
        Stats stats = playSimulatorGame2();
        assertEquals(2, stats.getWallsHit());
    }

    //collects all bottles in less than max turns
    private Stats playSimulatorGame3() {
        Grid grid = new Grid();

        Field[][] fields = new Field[][] {
                {Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY},
                {Field.BOTTLE/*start*/, Field.BOTTLE, Field.EMPTY, Field.EMPTY},
                {Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY},
                {Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY}
        };
        grid.setGrid(fields, 1, 0);

        Algorithm algorithm = mock(Algorithm.class);

        //collect
        when(algorithm.nextMove(Field.BOTTLE, Field.WALL, Field.BOTTLE, Field.EMPTY, Field.EMPTY)).thenReturn(Move.COLLECT);

        //move right
        when(algorithm.nextMove(Field.EMPTY, Field.WALL, Field.BOTTLE, Field.EMPTY, Field.EMPTY)).thenReturn(Move.RIGHT);

        //move up
        when(algorithm.nextMove(Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY)).thenReturn(Move.COLLECT);

        Simulator simulator = new Simulator(5);
        return simulator.playGame(algorithm, grid, new Random());
    }

    @Test
    public void testSimulatorGame3BottlesCount() {
        Stats stats = playSimulatorGame3();
        assertEquals(2, stats.getBottlesCollected());
    }

    @Test
    public void testSimulatorGame3BottlesLeft() {
        Stats stats = playSimulatorGame3();
        assertEquals(0, stats.getBottlesLeft());
    }

    @Test
    public void testSimulatorGame3EmptyPickups() {
        Stats stats = playSimulatorGame3();
        assertEquals(0, stats.getEmptyPickups());
    }

    @Test
    public void testSimulatorGame3MovesNeeded() {
        Stats stats = playSimulatorGame3();
        assertEquals(3, stats.getMovesNeeded());
    }

    @Test
    public void testSimulatorGame3WallsHit() {
        Stats stats = playSimulatorGame3();
        assertEquals(0, stats.getWallsHit());
    }

    //skip all the turns
    private Stats playSimulatorGame4() {
        Grid grid = new Grid();

        Field[][] fields = new Field[][] {
                {Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY},
                {Field.BOTTLE, Field.BOTTLE /*start*/, Field.EMPTY, Field.EMPTY},
                {Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY},
                {Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY}
        };
        grid.setGrid(fields, 1, 1);

        Algorithm algorithm = mock(Algorithm.class);

        //random move
        when(algorithm.nextMove(Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY)).thenReturn(Move.SKIP_TURN);

        Simulator simulator = new Simulator(5);
        return simulator.playGame(algorithm, grid, new Random());
    }

    @Test
    public void testSimulatorGame4BottlesCount() {
        Stats stats = playSimulatorGame4();
        assertEquals(0, stats.getBottlesCollected());
    }

    @Test
    public void testSimulatorGame4BottlesLeft() {
        Stats stats = playSimulatorGame4();
        assertEquals(2, stats.getBottlesLeft());
    }

    @Test
    public void testSimulatorGame4EmptyPickups() {
        Stats stats = playSimulatorGame4();
        assertEquals(0, stats.getEmptyPickups());
    }

    @Test
    public void testSimulatorGame4MovesNeeded() {
        Stats stats = playSimulatorGame4();
        assertEquals(5, stats.getMovesNeeded());
    }

    @Test
    public void testSimulatorGame4WallsHit() {
        Stats stats = playSimulatorGame4();
        assertEquals(0, stats.getWallsHit());
    }

    @Test
    public void testRandomness() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Random random = mock(Random.class);

        Method method = AbstractSimulator.class.getDeclaredMethod("getRandomMove", Random.class);
        method.setAccessible(true);



        Map<Integer, Move> moves = new HashMap<>();
        for(int i = 0; i < 4; i++) {
            when(random.nextInt(4)).thenReturn(i);

            Move move = (Move) method.invoke(null, random);
            moves.put(i, move);
        }

        for(int i = 0; i < 1000; i++) {
            int mod = i % 4;

            when(random.nextInt(4)).thenReturn(mod);
            Move move = (Move) method.invoke(null, random);

            assertEquals(moves.get(mod), move);
        }
    }
}
