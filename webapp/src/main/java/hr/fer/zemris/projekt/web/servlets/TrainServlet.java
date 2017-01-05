package hr.fer.zemris.projekt.web.servlets;

import hr.fer.zemris.projekt.Algorithms;
import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
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

        ObservableAlgorithm algorithm = Algorithms.getAlgorithm(algorithmID);
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

        //content type must be set to text/event-stream
        resp.setContentType("text/event-stream");

        //encoding must be set to UTF-8
        resp.setCharacterEncoding("UTF-8");

        algorithm.addObserver((sender, observation) -> {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("iteration", observation.getIteration());
            jsonObject.put("best", observation.getBestResult().standardizedFitness());
            jsonObject.put("average", observation.getAverageFitness());

            try {
                resp.getWriter().write("data: " + jsonObject.toString() + "\n\n");

                if (observation.getIteration() % 20 == 0) {
                    resp.getWriter().flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Robot robot = algorithm.run(simulator, parameters);
        req.getSession().setAttribute("robot", robot);

        resp.getWriter().write("data: finished\n\n");
        resp.getWriter().flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
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
