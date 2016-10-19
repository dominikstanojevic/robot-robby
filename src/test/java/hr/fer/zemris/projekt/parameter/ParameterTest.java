package hr.fer.zemris.projekt.parameter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("javadoc")
public class ParameterTest {

    @Test
    public void testConstructorWithDefaultValues() {
        Parameter p = new Parameter("PopulationSize", ParameterType.INTEGER);

        assertEquals(p.getName(), "PopulationSize");
        assertEquals(p.getType(), ParameterType.INTEGER);
        assertEquals(Parameter.DEFAULT_MIN_VALUE, p.getMinValue(), 0);
        assertEquals(Parameter.DEFAULT_MAX_VALUE, p.getMaxValue(), 0);
        assertEquals(0, p.getValue(), 0);
    }

    @Test
    public void testConstructorWithValues() {
        Parameter p = new Parameter("PopulationSize", ParameterType.INTEGER, -100, 100, 20);

        assertEquals(p.getName(), "PopulationSize");
        assertEquals(p.getType(), ParameterType.INTEGER);
        assertEquals(-100, p.getMinValue(), 0);
        assertEquals(100, p.getMaxValue(), 0);
        assertEquals(20, p.getValue(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNameNull() {
        new Parameter(null, ParameterType.INTEGER, -100, 100, 20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithTypeNull() {
        new Parameter("TournamentSize", null, -100, 100, 20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithImpossibleValues() {
        new Parameter("TournamentSize", ParameterType.INTEGER, -100, 100, 120);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithImpossibleValues2() {
        new Parameter("TournamentSize", ParameterType.INTEGER, 100, -100, 120);
    }

    @Test
    public void testSetValue() {
        Parameter p = new Parameter("PopulationSize", ParameterType.INTEGER, 0, 100, 0);
        assertEquals(0, p.getValue(), 0);

        p.setValue(40);
        assertEquals(40, p.getValue(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueLessThanMin() {
        Parameter p = new Parameter("PopulationSize", ParameterType.INTEGER, 0, 100, 0);
        p.setValue(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueGreaterThanMax() {
        Parameter p = new Parameter("PopulationSize", ParameterType.INTEGER, 0, 100, 0);
        p.setValue(110);
    }


    @Test
    public void testSetMinValue() {
        Parameter p = new Parameter("Name", ParameterType.DOUBLE, 0, 100, 0);
        assertEquals(0, p.getMinValue(), 0);

        p.setMinValue(-20);
        assertEquals(-20, p.getMinValue(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMinValueGreaterThanValue() {
        Parameter p = new Parameter("PopulationSize", ParameterType.INTEGER, 0, 100, 0);
        p.setMinValue(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMinValueGreaterThanMaxValue() {
        Parameter p = new Parameter("PopulationSize", ParameterType.INTEGER, 0, 100, 0);
        p.setMinValue(110);
    }

    @Test
    public void testSetMaxValue() {
        Parameter p = new Parameter("Name", ParameterType.DOUBLE, 0, 100, 0);
        assertEquals(100, p.getMaxValue(), 0);

        p.setMaxValue(110);
        assertEquals(110, p.getMaxValue(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxValueLessThanValue() {
        Parameter p = new Parameter("PopulationSize", ParameterType.INTEGER, 0, 100, 50);
        p.setMaxValue(40);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxValueLessThanMinValue() {
        Parameter p = new Parameter("PopulationSize", ParameterType.INTEGER, 0, 100, 50);
        p.setMaxValue(-10);
    }
}
