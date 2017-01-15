package hr.fer.zemris.projekt.web.servlets.training;

import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.web.servlets.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

@WebServlet(name = "StopTrainingServlet", urlPatterns = {"/stopTraining"})
public class StopTrainingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExecutorService executor = (ExecutorService) req.getSession().getAttribute(Constants.SESSION_KEY_TRAINING_THREAD);
        if (executor == null) {
            resp.setStatus(400);
            return;
        }

        FutureTask<Robot> task = (FutureTask<Robot>) req.getSession().getAttribute(Constants.SESSION_KET_TRAINING_TASK);

        req.getSession().removeAttribute(Constants.SESSION_KEY_TRAINING_THREAD);
        req.getSession().removeAttribute(Constants.SESSION_KET_TRAINING_TASK);

        task.cancel(true);
    }
}
