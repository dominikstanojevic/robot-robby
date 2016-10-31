package hr.fer.zemris.projekt.simulator;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.grid.Field;
import hr.fer.zemris.projekt.grid.Grid;
import hr.fer.zemris.projekt.grid.IGrid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

// Parametrized test class which tests both the Simulator and MultithreadedSimulator
@SuppressWarnings("javadoc")
@RunWith(value = Parameterized.class)
public class SimulatorTest {

    private Function<Integer, AbstractSimulator> simulatorConstructor;
    private Supplier<AbstractSimulator> defaultConstructor;

    public SimulatorTest(Function<Integer, AbstractSimulator> simulatorConstructor, Supplier<AbstractSimulator> defaultConstructor) {
        this.simulatorConstructor = simulatorConstructor;
        this.defaultConstructor = defaultConstructor;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList( new Object[][] {
                {(Function<Integer, AbstractSimulator>) Simulator::new, (Supplier<AbstractSimulator>) Simulator::new},
                {(Function<Integer, AbstractSimulator>) MultithreadedSimulator::new, (Supplier<AbstractSimulator>) MultithreadedSimulator::new}
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimulatorConstructorWithNegativeMoves() {
        simulatorConstructor.apply(-2);
    }

    // 2 pickups and 1 empty pickups, along with moving down and left
    private Stats playSimulatorGame1() {
        IGrid grid = new Grid();

        Field[][] fields = new Field[][] {
                { Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY },
                { Field.EMPTY, Field.EMPTY, Field.BOTTLE /*start*/, Field.EMPTY },
                { Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY },
                { Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.BOTTLE } };
        grid.setGrid(fields, 1, 2);

        Robot robot = mock(Robot.class);

        //collect
        when(robot.nextMove(Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY))
                .thenReturn(Move.COLLECT);

        //move left
        when(robot.nextMove(Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY)).thenReturn(Move.LEFT);

        //move down
        when(robot.nextMove(Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.BOTTLE))
                .thenReturn(Move.DOWN);

        //collect
        when(robot.nextMove(Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY))
                .thenReturn(Move.COLLECT);

        //do an empty pickup
        when(robot.nextMove(Field.EMPTY, Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY))
                .thenReturn(Move.COLLECT);

        AbstractSimulator simulator = simulatorConstructor.apply(5);
        return simulator.playGame(robot, grid, new Random());
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
        IGrid grid = new Grid();

        Field[][] fields = new Field[][] {
                { Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY },
                { Field.EMPTY, Field.EMPTY, Field.BOTTLE, Field.EMPTY },
                { Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY },
                { Field.EMPTY, Field.EMPTY, Field.EMPTY/*start*/, Field.BOTTLE } };
        grid.setGrid(fields, 3, 2);

        Robot robot = mock(Robot.class);

        //move right
        when(robot.nextMove(Field.EMPTY, Field.EMPTY, Field.BOTTLE, Field.EMPTY, Field.WALL))
                .thenReturn(Move.RIGHT);

        //collect
        when(robot.nextMove(Field.BOTTLE, Field.EMPTY, Field.WALL, Field.EMPTY, Field.WALL))
                .thenReturn(Move.COLLECT);

        //move up
        when(robot.nextMove(Field.EMPTY, Field.EMPTY, Field.WALL, Field.EMPTY, Field.WALL)).thenReturn(Move.UP);

        //move right
        when(robot.nextMove(Field.EMPTY, Field.EMPTY, Field.WALL, Field.EMPTY, Field.EMPTY)).thenReturn(Move.RIGHT);

        AbstractSimulator simulator = simulatorConstructor.apply(5);
        return simulator.playGame(robot, grid, new Random());
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
        IGrid grid = new Grid();

        Field[][] fields = new Field[][] {
                { Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY },
                { Field.BOTTLE/*start*/, Field.BOTTLE, Field.EMPTY, Field.EMPTY },
                { Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY },
                { Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY } };
        grid.setGrid(fields, 1, 0);

        Robot robot = mock(Robot.class);

        //collect
        when(robot.nextMove(Field.BOTTLE, Field.WALL, Field.BOTTLE, Field.EMPTY, Field.EMPTY))
                .thenReturn(Move.COLLECT);

        //move right
        when(robot.nextMove(Field.EMPTY, Field.WALL, Field.BOTTLE, Field.EMPTY, Field.EMPTY))
                .thenReturn(Move.RIGHT);

        //move up
        when(robot.nextMove(Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY))
                .thenReturn(Move.COLLECT);

        AbstractSimulator simulator = simulatorConstructor.apply(5);
        return simulator.playGame(robot, grid, new Random());
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
        IGrid grid = new Grid();

        Field[][] fields = new Field[][] {
                { Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY },
                { Field.BOTTLE, Field.BOTTLE /*start*/, Field.EMPTY, Field.EMPTY },
                { Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY },
                { Field.EMPTY, Field.EMPTY, Field.EMPTY, Field.EMPTY } };
        grid.setGrid(fields, 1, 1);

        Robot robot = mock(Robot.class);

        //random move
        when(robot.nextMove(Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.EMPTY))
                .thenReturn(Move.SKIP_TURN);

        AbstractSimulator simulator = simulatorConstructor.apply(5);
        return simulator.playGame(robot, grid, new Random());
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
        for (int i = 0; i < 4; i++) {
            when(random.nextInt(4)).thenReturn(i);

            Move move = (Move) method.invoke(null, random);
            moves.put(i, move);
        }

        for (int i = 0; i < 1000; i++) {
            int mod = i % 4;

            when(random.nextInt(4)).thenReturn(mod);
            Move move = (Move) method.invoke(null, random);

            assertEquals(moves.get(mod), move);
        }
    }

    private List<Path> getPaths() {
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get("src/test/files/Grid1.txt"));
        paths.add(Paths.get("src/test/files/Grid2.txt"));
        paths.add(Paths.get("src/test/files/Grid3.txt"));

        return paths;
    }

    @Test
    public void testLoadingGridFromFile() throws IOException {
        Robot robot = mock(Robot.class);
        when(robot.nextMove(Mockito.any(Field.class), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Move.RANDOM);

        AbstractSimulator simulator = simulatorConstructor.apply(5);
        simulator.readGridFromFile(getPaths());

        AbstractSimulator spy = Mockito.spy(simulator);
        spy.playGames(robot);
        Mockito.verify(spy, times(3)).playGame(eq(robot), Mockito.any(IGrid.class), Mockito.any(Random.class));
    }

    @Test
    public void testGridGenerator() throws NoSuchFieldException, IllegalAccessException {
        int size = 3;
        int numberOfBottles = 10;
        int width = 4;
        int height = 7;

        AbstractSimulator simulator = defaultConstructor.get();
        simulator.generateGrids(size, numberOfBottles, width, height, false);

        java.lang.reflect.Field field = AbstractSimulator.class.getDeclaredField("grids");
        field.setAccessible(true);

        IGrid[] grids = (IGrid[]) field.get(simulator);
        assertEquals(3, grids.length);

        for(IGrid grid : grids) {
            assertEquals(numberOfBottles, grid.getNumberOfBottles());
            assertEquals(width, grid.getWidth());
            assertEquals(height, grid.getHeight());
        }
    }

    @Test
    public void gridSetTest() throws NoSuchFieldException, IllegalAccessException {
        IGrid grid = new Grid();

        AbstractSimulator simulator = defaultConstructor.get();
        simulator.setGrid(grid);

        java.lang.reflect.Field field = AbstractSimulator.class.getDeclaredField("grids");
        field.setAccessible(true);

        IGrid[] grids = (IGrid[]) field.get(simulator);
        assertEquals(1, grids.length);

        assertEquals(grid, grids[0]);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidFile() throws IOException {
        AbstractSimulator simulator = defaultConstructor.get();

        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get("ovajFileSigurnoNePostoji"));


        simulator.readGridFromFile(paths);
    }
}
