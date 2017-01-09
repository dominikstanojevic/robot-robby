package hr.fer.zemris.projekt.web.servlets;

import hr.fer.zemris.projekt.simulator.Simulator;
import hr.fer.zemris.projekt.web.utils.SimulatorConfiguration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

@WebServlet(name = "PauseTrainingServlet", urlPatterns = "/pauseTraining")
public class PauseTrainingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExecutorService executor = (ExecutorService) req.getSession().getAttribute(Constants.SESSION_KEY_TRAINING_THREAD);
        if (executor == null) {
            return;
        }

        SimulatorConfiguration simulatorConfig = (SimulatorConfiguration)
                req.getSession().getAttribute(Constants.SESSION_KEY_SIMULATOR_CONFIG);

        Simulator simulator = simulatorConfig.getSimulator();


    }
}
