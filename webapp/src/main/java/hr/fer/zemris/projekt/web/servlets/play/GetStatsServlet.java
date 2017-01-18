package hr.fer.zemris.projekt.web.servlets.play;

import com.google.gson.Gson;
import hr.fer.zemris.projekt.Algorithms;
import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.neural.elman.ga.GA;
import hr.fer.zemris.projekt.grid.Grid;
import hr.fer.zemris.projekt.simulator.Simulator;
import hr.fer.zemris.projekt.simulator.Stats;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
@MultipartConfig
@WebServlet("/getStats")
public class GetStatsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson GSON = new Gson();

        String gridJSON = req.getParameter("grid");
        Grid grid = GSON.fromJson(gridJSON, Grid.class);

        Simulator simulator = new Simulator();
        simulator.setGrid(grid);

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(getRobotScore("ga", simulator, req)).append(",");
        sb.append(getRobotScore("gp", simulator, req)).append(",");
        sb.append(getRobotScore("nn", simulator, req)).append(",");
        sb.append(getRobotScore("elman", simulator, req)).append(",");
        sb.append(getRobotScore("reinforcement", simulator, req)).append("]");

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(sb.toString());
        resp.getWriter().flush();
    }

    private String getRobotScore(String id, Simulator simulator, HttpServletRequest req) throws IOException {
        Algorithm algorithm = Algorithms.getAlgorithm(id);
        Path robotFilePath = Paths.get(req.getServletContext().getRealPath("resources/robots") + "/" + id + ".robby");
        Robot robot = algorithm.readSolutionFromFile(robotFilePath);
        Stats stat = simulator.playGames(robot).get(0);

        int score = 0;
        score += stat.getBottlesCollected() * 10;
        score -= stat.getEmptyPickups();
        score -= stat.getWallsHit() * 5;

        return "\"" + algorithm.toString() + ": " + score + "\"";
    }
}
