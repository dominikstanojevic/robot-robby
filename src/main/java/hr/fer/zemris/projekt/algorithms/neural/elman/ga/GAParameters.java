package hr.fer.zemris.projekt.algorithms.neural.elman.ga;

import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.Parameters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dominik on 9.12.2016..
 */
public class GAParameters implements Parameters<GA> {
    private Map<String, Parameter> parameters = new HashMap<>();

    @Override
    public Parameter getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public void setParameter(String name, double value) {
        Parameter parameter = new Parameter(name, )
    }

    @Override
    public Set<Parameter> getParameters() {
        return null;
    }
}
