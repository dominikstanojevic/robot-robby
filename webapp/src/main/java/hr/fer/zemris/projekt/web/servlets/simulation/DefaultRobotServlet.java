package hr.fer.zemris.projekt.web.servlets.simulation;

import hr.fer.zemris.projekt.Algorithms;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.web.servlets.Constants;
import hr.fer.zemris.projekt.web.utils.Utility;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
@MultipartConfig
@WebServlet("/loadRobot")
public class DefaultRobotServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String algorithmID = req.getParameter("algorithmID");
        ObservableAlgorithm algorithm = Algorithms.getAlgorithm(algorithmID);
        req.getSession().setAttribute(Constants.SESSION_KEY_ALGORITHM, algorithm);

        Path robotFilePath = Paths.get(req.getServletContext().getRealPath("resources/robots") + "/" + algorithmID + ".robby");
        Robot robot = algorithm.readSolutionFromFile(robotFilePath);

        req.getSession().setAttribute(Constants.SESSION_KEY_ROBOT, robot);
    }
}
