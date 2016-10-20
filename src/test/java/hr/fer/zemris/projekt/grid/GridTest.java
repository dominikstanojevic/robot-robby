package hr.fer.zemris.projekt.grid;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("javadoc")
public class GridTest {

    private Grid grid;

    @Before
    public void setup() {
        grid = new Grid();
    }

    @Test
    public void testGeneration() {
        grid.generate(10, 15, 30, false);

        assertEquals(30, grid.getNumberOfBottles());
        assertEquals(15, grid.getHeight());
        assertEquals(10, grid.getWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerationWithTooManyBottles() {
        grid.generate(10, 10, 101, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerationWithNegativeNumberOfBottles() {
        grid.generate(10, 10, -1, false);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testGenerationWithNegativeWidth() {
        grid.generate(-10, 10, 20, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerationWithNegativeHeight() {
        grid.generate(10, -10, 20, false);
    }

    @Ignore
    @Test
    public void testGenerationWithWalls() {
        grid.generate(10, 10, 20, true);
    }

    @Test
    public void testTileGetterAndSetter() {
        grid.generate(10, 10, 20, false);

        grid.setField(3, 3, Field.EMPTY);
        assertEquals(Field.BOTTLE, grid.getField(3, 3));

        grid.setField(8, 6, Field.BOTTLE);
        assertEquals(Field.BOTTLE, grid.getField(8, 6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalGetter() {
        grid.generate(10, 10, 20, false);
        grid.getField(20, 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalSetter() {
        grid.generate(10, 10, 20, false);
        grid.setField(20, 9, Field.BOTTLE);
    }

    @Test
    public void testPlacingNewBottle() {
        grid.generate(10, 10, 20, false);
        grid.setField(0, 0, Field.EMPTY);
        int bottles = grid.getNumberOfBottles();

        grid.setField(0, 0, Field.BOTTLE);
        assertEquals(bottles + 1, grid.getNumberOfBottles());
    }

    @Test
    public void testCustomGrid() {
        Field[][] fields = new Field[][] {
                {Field.BOTTLE, Field.EMPTY, Field.EMPTY},
                {Field.EMPTY, Field.EMPTY, Field.BOTTLE},
                {Field.BOTTLE, Field.BOTTLE, Field.EMPTY}
        };
        grid.setGrid(fields, 1, 1);

        assertEquals(4, grid.getNumberOfBottles());
        assertEquals(Field.BOTTLE, grid.getField(1, 2));
        assertTrue(grid.hasBottlesLeft());

        assertEquals(1, grid.getCurrentColumn());
        assertEquals(1, grid.getCurrentRow());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomNullGrid() {
        grid.setGrid(null, 0, 0);
    }
}
