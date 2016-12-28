package hr.fer.zemris.projekt.web.servlets;

import hr.fer.zemris.projekt.Algorithms;
import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.ParameterType;
import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.Simulator;
import hr.fer.zemris.projekt.web.utils.SimulatorConfiguration;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "TrainServlet", urlPatterns = {"/train"})

public class TrainServlet extends HttpServlet {

    private static final Random RANDOM = new Random();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String algorithmID = req.getParameter("algorithmID");

        Algorithm algorithm = Algorithms.getAlgorithm(algorithmID);
        Parameters<?> parameters = readParameters(req, algorithm);

        SimulatorConfiguration config = (SimulatorConfiguration) req.getSession().getAttribute("simulatorConfig");
        Simulator simulator = config.getSimulator();

        if (config.isVariableBottles()) {
            simulator.generateGrids(
                    config.getNumberOfGrids(),
                    config.getGridWidth(),
                    config.getGridHeight(),
                    false,
                    RANDOM
            );
        } else {
            simulator.generateGrids(
                    config.getNumberOfGrids(),
                    config.getNumberOfBottles(),
                    config.getGridWidth(),
                    config.getGridHeight(),
                    false
            );
        }

        Robot robot = algorithm.run(simulator, parameters);
        req.getSession().setAttribute("robot", robot);

        resp.setContentType("application/json;charset=UTF-8");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fitness", robot.standardizedFitness());

        resp.getWriter().write(jsonObject.toString());
        resp.getWriter().flush();
    }

    private Parameters<?> readParameters(HttpServletRequest req, Algorithm algorithm) {
        Parameters<? extends Algorithm> parameters = algorithm.getDefaultParameters();
        Set<String> parameterNames = parameters.getParameters().stream()
                .map(Parameter::getName)
                .collect(Collectors.toSet());

        for (String parameterName : parameterNames) {
            String stringValue = req.getParameter(parameterName);
            if (stringValue == null) continue;

            ParameterType type = parameters.getParameter(parameterName).getType();

            if (type == ParameterType.DOUBLE) {
                double value = Double.parseDouble(stringValue);
                parameters.setParameter(parameterName, value);

            } else if (type == ParameterType.INTEGER) {
                int value = Integer.parseInt(stringValue);
                parameters.setParameter(parameterName, value);

            } else {
                throw new IllegalArgumentException("Unknown parameter type.");
            }
        }

        return parameters;
    }
}
