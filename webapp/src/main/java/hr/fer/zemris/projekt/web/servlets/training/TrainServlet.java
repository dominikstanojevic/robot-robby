package hr.fer.zemris.projekt.web.servlets.training;

import hr.fer.zemris.projekt.Algorithms;
import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.ParameterType;
import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.Simulator;
import hr.fer.zemris.projekt.web.servlets.Constants;
import hr.fer.zemris.projekt.web.utils.MapGeneration;
import hr.fer.zemris.projekt.web.utils.SimulatorConfiguration;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@WebServlet(name = "TrainServlet", urlPatterns = {"/train"})
public class TrainServlet extends HttpServlet {

    private static final int TIMEOUT_MINUTES = 10;
    private static final int FLUSH_FREQUENCY = 10;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //read the parameters and generate grids
        String algorithmID = req.getParameter("algorithmID");

        ObservableAlgorithm algorithm = Algorithms.getAlgorithm(algorithmID);
        Parameters<?> parameters = readParameters(req, algorithm);

        SimulatorConfiguration config = (SimulatorConfiguration) req.getSession().getAttribute(Constants.SESSION_KEY_SIMULATOR_CONFIG);
        Simulator simulator = config.getSimulator();

        MapGeneration.generateGrids(simulator, config);

        int mapRegenFrequency = config.getMapRegenFrequency();

        //configure the observer
        algorithm.addObserver((sender, observation) -> {
            req.getSession().setAttribute(Constants.SESSION_KEY_ROBOT, observation.getBestResult());

            if (mapRegenFrequency > 0 && observation.getIteration() % mapRegenFrequency == 0) {
                MapGeneration.generateGrids(simulator, config);
            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("iteration", observation.getIteration());
            jsonObject.put("best", observation.getBestResult().standardizedFitness());
            jsonObject.put("average", observation.getAverageFitness());

            try {
                resp.getWriter().write("data: " + jsonObject.toString() + "\n\n");

                if (observation.getIteration() % FLUSH_FREQUENCY == 0) {
                    resp.getWriter().flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //set up the training thread and store in the session
        FutureTask<Robot> futureTask = new FutureTask<>(() -> algorithm.run(simulator, parameters));

        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

        req.getSession().setAttribute(Constants.SESSION_KEY_SIMULATOR, simulator);
        req.getSession().setAttribute(Constants.SESSION_KEY_TRAINING_THREAD, executor);
        req.getSession().setAttribute(Constants.SESSION_KET_TRAINING_TASK, futureTask);
        req.getSession().setAttribute(Constants.SESSION_KEY_ALGORITHM, algorithm);

        executor.submit(futureTask);

        try {
            //TODO tweak this in the event the training is paused?
            Robot robot = futureTask.get(TIMEOUT_MINUTES, TimeUnit.MINUTES);
            req.getSession().setAttribute(Constants.SESSION_KEY_ROBOT, robot);

        } catch (TimeoutException e) {
            futureTask.cancel(true);
            System.err.println("Training thread timed out.");

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Training thread went to hell.");
            e.printStackTrace();

        } finally {
            req.getSession().removeAttribute(Constants.SESSION_KEY_TRAINING_THREAD);
            req.getSession().removeAttribute(Constants.SESSION_KET_TRAINING_TASK);
            req.getSession().removeAttribute(Constants.SESSION_KEY_SIMULATOR);

            executor.shutdown();

            resp.getWriter().write("data: finished\n\n");
            resp.getWriter().flush();
        }
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
