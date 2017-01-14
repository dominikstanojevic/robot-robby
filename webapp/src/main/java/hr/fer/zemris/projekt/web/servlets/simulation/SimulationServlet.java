package hr.fer.zemris.projekt.web.servlets.simulation;

import com.google.gson.Gson;
import hr.fer.zemris.projekt.Algorithms;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.grid.Grid;
import hr.fer.zemris.projekt.simulator.Simulator;
import hr.fer.zemris.projekt.simulator.Stats;
import hr.fer.zemris.projekt.web.servlets.Constants;
import hr.fer.zemris.projekt.web.utils.SimulatorConfiguration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SimulationServlet", urlPatterns = {"/simulation"})
public class SimulationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("algorithms", Algorithms.getAvailableAlgoritmhs());

        req.getRequestDispatcher("./simulation.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson GSON = new Gson();

        req.setAttribute("algorithms", Algorithms.getAvailableAlgoritmhs());

        SimulatorConfiguration simulatorConfiguration = new SimulatorConfiguration();
        Simulator simulator = simulatorConfiguration.getSimulator();

        Robot robot = (Robot)req.getSession().getAttribute(Constants.SESSION_KEY_ROBOT);
        Grid grid = (Grid)req.getSession().getAttribute(Constants.SESSION_KEY_GRID);

        simulator.setGrid(grid);
        Stats stats = simulator.playGames(robot).get(0);

        req.setAttribute("grid", GSON.toJson(grid));
        req.setAttribute("moves", GSON.toJson(stats.getMoves()));

        req.getRequestDispatcher("./simulation.jsp").forward(req, resp);
    }

}
