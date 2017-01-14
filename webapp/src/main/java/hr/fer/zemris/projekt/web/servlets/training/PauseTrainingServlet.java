package hr.fer.zemris.projekt.web.servlets.training;

import hr.fer.zemris.projekt.simulator.Simulator;
import hr.fer.zemris.projekt.web.servlets.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

@WebServlet(name = "PauseTrainingServlet", urlPatterns = "/pauseTraining")
public class PauseTrainingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExecutorService executor = (ExecutorService) req.getSession().getAttribute(Constants.SESSION_KEY_TRAINING_THREAD);
        if (executor == null) {
            return;
        }

        Simulator simulator = (Simulator) req.getSession().getAttribute(Constants.SESSION_KEY_SIMULATOR);
        simulator.suspend();
    }
}
