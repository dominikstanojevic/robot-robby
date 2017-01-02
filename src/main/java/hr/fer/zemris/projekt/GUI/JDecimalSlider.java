package hr.fer.zemris.projekt.GUI;

import hr.fer.zemris.projekt.parameter.Parameter;

import java.util.Hashtable;

import javax.swing.JLabel;

public class JDecimalSlider extends JParameterSlider {

    private static final long serialVersionUID = -4962727908338057096L;

    private double potention = 1;
    private int decimalSpots;

    public JDecimalSlider(double min, double max, double value, int decimalSpots,
            Parameter parameter, JLabel lCurrentValue) {
        super(parameter, lCurrentValue);

        if (decimalSpots < 0)
            throw new IllegalArgumentException(
                    "Number of decimal spots must be greater of equal to zero.");
        this.decimalSpots = decimalSpots;

        potention = (int) Math.round(Math.pow(10, decimalSpots));
        int intMin = (int) Math.round(min * potention);
        int intMax = (int) Math.round(max * potention);
        int intValue = (int) Math.round(value * potention);

        super.setMinimum(intMin);
        super.setMaximum(intMax);
        super.setValue(intValue);

        Hashtable labelTable = new Hashtable();
        labelTable.put(intMin, new JLabel(String.format("%." + decimalSpots + "f", min)));
        labelTable.put(intMax, new JLabel(String.format("%." + decimalSpots + "f", max)));
        setLabelTable(labelTable);

        setPaintLabels(true);

        changeValueDisplay(value);
    }

    public double getDoubleValue() {
        return super.getValue() / potention;
    }

    public void changeValueDisplay(double value) {
        lCurrentValue.setText(String.format("%." + decimalSpots + "f", value));
    }

    @Override
    public void setParameter(double value) {
        parameter.setValue(value / potention);
    }

}
