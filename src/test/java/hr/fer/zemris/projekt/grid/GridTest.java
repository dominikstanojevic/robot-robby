package hr.fer.zemris.projekt.grid;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("javadoc")
public class GridTest {
    private String pathPrefix = "src/test/java/hr/fer/zemris/projekt/grid/";
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
        assertEquals(Field.EMPTY, grid.getField(3, 3));

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

    @Test
    public void testCustomGrid2() {
        Field[][] fields = new Field[][] {
                {Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.BOTTLE},
                {Field.WALL, Field.EMPTY, Field.BOTTLE, Field.WALL},
                {Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY}
        };
        int row = 1;
        int column = 2;
        grid.setGrid(fields, row, column);
        assertEquals(3, grid.getHeight());
        assertEquals(4, grid.getWidth());

        assertEquals(Field.WALL, grid.getField(-1, 2));
        assertEquals(Field.WALL, grid.getField(-1, -1));
        assertEquals(Field.WALL, grid.getField(3, 1));

        assertEquals(Field.EMPTY, grid.getField(1, 1));
        assertEquals(Field.EMPTY, grid.getField(0, 1));
        assertEquals(Field.WALL, grid.getField(1, 0));

        assertEquals(5, grid.getNumberOfBottles());
        assertEquals(Field.BOTTLE, grid.getField(1, 2));
        assertTrue(grid.hasBottlesLeft());

        assertEquals(column, grid.getCurrentColumn());
        assertEquals(row, grid.getCurrentRow());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadFromFile(){
        Path filePath = Paths.get(pathPrefix).resolve("gridDefinition1.txt");

        try {
            grid.readFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadFromFile2(){
        Path filePath = Paths.get(pathPrefix).resolve("gridDefinition2.txt");

        try {
            grid.readFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(3, grid.getHeight());
        assertEquals(4, grid.getWidth());

        assertEquals(2, grid.getCurrentColumn());
        assertEquals(1, grid.getCurrentRow());

        assertEquals(Field.EMPTY, grid.getField(0, 0));
        assertEquals(Field.BOTTLE, grid.getField(0, 1));
        assertEquals(Field.EMPTY, grid.getField(0, 2));
        assertEquals(Field.EMPTY, grid.getField(0, 3));

        assertEquals(Field.WALL, grid.getField(1, 0));
        assertEquals(Field.EMPTY, grid.getField(1, 1));
        assertEquals(Field.BOTTLE, grid.getField(1, 2));
        assertEquals(Field.BOTTLE, grid.getField(1, 3));

        assertEquals(Field.WALL, grid.getField(2, 0));
        assertEquals(Field.EMPTY, grid.getField(2, 1));
        assertEquals(Field.EMPTY, grid.getField(2, 2));
        assertEquals(Field.WALL, grid.getField(2, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadEmptyFile(){
        Path filePath = Paths.get(pathPrefix).resolve("emptyFile.txt");

        try {
            grid.readFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadNullFile(){
        try {
            grid.readFromFile(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadFileThatDoesNotExist(){
        Path filePath = Paths.get(pathPrefix).resolve("gridDefinitionNan.txt");

        try {
            grid.readFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadFileWrongSize(){
        Path filePath = Paths.get(pathPrefix).resolve("invalidSize.txt");

        try {
            grid.readFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadFileSizeNaN(){
        Path filePath = Paths.get(pathPrefix).resolve("sizeNaN.txt");

        try {
            grid.readFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadFileWrongStart(){
        Path filePath = Paths.get(pathPrefix).resolve("invalidStart.txt");

        try {
            grid.readFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadFileStartNaN(){
        Path filePath = Paths.get(pathPrefix).resolve("startNaN.txt");

        try {
            grid.readFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void writeToFile(){
        Path inputPath = Paths.get(pathPrefix).resolve("gridDefinition2.txt");
        Path outputPath = Paths.get(pathPrefix).resolve("gridOutput.txt");

        try {
            grid.readFromFile(inputPath);
            grid.writeToFile(outputPath);

            List<String> input = Files.readAllLines(inputPath);
            List<String> output = Files.readAllLines(outputPath);

            assertEquals(input.size(), output.size());
            int n = input.size();

            for (int i = 0; i < n; ++i){
                assertEquals(input.get(i), output.get(i));
            }

            Files.delete(outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCopy(){
        Field[][] fields = new Field[][] {
                {Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.BOTTLE},
                {Field.WALL, Field.EMPTY, Field.BOTTLE, Field.WALL},
                {Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY}
        };
        int row = 1;
        int column = 2;
        grid.setGrid(fields, row, column);

        IGrid grid2 = grid.copy();
        grid2.setField(0, 0, Field.EMPTY);
        assertEquals(Field.BOTTLE, grid.getField(0, 0));
        assertEquals(Field.EMPTY, grid2.getField(0, 0));
        assertEquals(5, grid.getNumberOfBottles());
        assertEquals(4, grid2.getNumberOfBottles());

        assertEquals(column, grid2.getCurrentColumn());
        assertEquals(row, grid2.getCurrentRow());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomNullGrid() {
        grid.setGrid(null, 0, 0);
    }

    @Test
    public void testSetStartingPosition() {
        Field[][] fields = new Field[][] {
                {Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.BOTTLE},
                {Field.WALL, Field.EMPTY, Field.BOTTLE, Field.WALL},
                {Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY}
        };
        int row = 0;
        int column = 0;
        grid.setGrid(fields, row, column);

        grid.setStartingPosition(2, 3);

        assertEquals(2, grid.getCurrentRow());
        assertEquals(3, grid.getCurrentColumn());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalStartingPosition() {
        Field[][] fields = new Field[][] {
                {Field.BOTTLE, Field.EMPTY, Field.EMPTY, Field.BOTTLE},
                {Field.WALL, Field.EMPTY, Field.BOTTLE, Field.WALL},
                {Field.BOTTLE, Field.BOTTLE, Field.EMPTY, Field.EMPTY}
        };
        int row = 0;
        int column = 0;
        grid.setGrid(fields, row, column);

        grid.setStartingPosition(2, 4);
    }
}
