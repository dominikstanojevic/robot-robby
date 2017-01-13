package hr.fer.zemris.projekt.GUI;

import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;

import hr.fer.zemris.projekt.parameter.Parameter;

public class JParameterSlider extends JSlider {
	
	private static final long serialVersionUID = -7478046803277819852L;
	Parameter parameter;
	JLabel lCurrentValue;

	public JParameterSlider(int min, int max, int value, Parameter parameter, JLabel lCurrentValue) {
		super(min, max, value);
		
		if(parameter == null) throw new IllegalArgumentException("Parameter cannot be null.");
		this.parameter = parameter;
		this.lCurrentValue = lCurrentValue;
		lCurrentValue.setText(Integer.toString(value));
		
		Hashtable labelTable = new Hashtable();
		labelTable.put(min, new JLabel(Integer.toString(min)));
		labelTable.put(max, new JLabel(Integer.toString(max)));
		setLabelTable(labelTable);
		
		setPaintLabels(true);		
	}
	
	
	public JParameterSlider(Parameter parameter, JLabel lCurrentValue) {
		super();
		this.parameter = parameter;
		this.lCurrentValue = lCurrentValue;
	}

	public void setParameter(double value){
		parameter.setValue(value);
	}


	public Parameter getParameter() {
		return parameter;
	}
	
	public void changeValueDisplay(int value){
		lCurrentValue.setText(Integer.toString(value));
	}
	
	
	
	

}
