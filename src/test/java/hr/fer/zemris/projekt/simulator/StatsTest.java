package hr.fer.zemris.projekt.simulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.grid.IGrid;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class StatsTest {

    private IGrid grid;
    private List<Move> moves;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        grid = mock(IGrid.class);
        moves = mock(List.class);
    }

    @Test
    public void testConstructor() {
        Stats s = new Stats(0, 1, 2, 3, 4, grid, moves);

        assertEquals(s.getMovesNeeded(), 0);
        assertEquals(s.getBottlesCollected(), 1);
        assertEquals(s.getBottlesLeft(), 2);
        assertEquals(s.getWallsHit(), 3);
        assertEquals(s.getEmptyPickups(), 4);

        assertSame(s.getGrid(), grid);
        assertSame(s.getMoves(), moves);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGrid() {
        new Stats(0, 1, 2, 3, 4, null, moves);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMovesList() {
        new Stats(0, 1, 2, 3, 4, grid, null);
    }
}
