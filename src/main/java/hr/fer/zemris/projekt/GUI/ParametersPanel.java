package hr.fer.zemris.projekt.GUI;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.ParameterType;
import hr.fer.zemris.projekt.parameter.Parameters;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ParametersPanel extends JPanel {

    private static final long serialVersionUID = -1700682549637305635L;

    public static final int DECIMAL_SPOTS = 2;

    Parameters<? extends Algorithm> parameters;

    public ParametersPanel() {
        super();
        initGUI();
    }

    private void initGUI() {
        setLayout(new GridLayout(0, 1));

    }

    public Parameters<? extends Algorithm> getParameters() {
        return parameters;
    }

    public void setParameters(Parameters<? extends Algorithm> parameters) {
        this.parameters = parameters;

        this.removeAll();

        Set<Parameter> paramSet = parameters.getParameters();

        if (paramSet != null) {
            for (Parameter param : paramSet) {

                JPanel labels = new JPanel(new FlowLayout());
                JLabel paramName = new JLabel(param.getName() + ":");
                labels.add(paramName);
                JLabel currValue = new JLabel();
                labels.add(currValue);
                add(labels);

                if (param.getType() == ParameterType.INTEGER) {

                    JParameterSlider slider = new JParameterSlider((int) Math.round(param
                            .getMinValue()), (int) Math.round(param.getMaxValue()),
                            (int) Math.round(param.getValue()), param, currValue);
                    add(slider);

                    slider.addChangeListener(new ChangeListener() {

                        @Override
                        public void stateChanged(ChangeEvent e) {
                            slider.changeValueDisplay(slider.getValue());
                            slider.setParameter(slider.getValue());
                        }
                    });

                } else if (param.getType() == ParameterType.DOUBLE) {

                    JDecimalSlider slider = new JDecimalSlider(param.getMinValue(),
                            param.getMaxValue(), param.getValue(), DECIMAL_SPOTS, param, currValue);
                    add(slider);

                    slider.addChangeListener(new ChangeListener() {

                        @Override
                        public void stateChanged(ChangeEvent e) {
                            slider.changeValueDisplay(slider.getDoubleValue());
                            slider.setParameter(slider.getValue());

                        }
                    });

                }

            }

        }

    }

}
