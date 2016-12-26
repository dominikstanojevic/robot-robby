package hr.fer.zemris.projekt.web.servlets;

import hr.fer.zemris.projekt.simulator.Simulator;
import hr.fer.zemris.projekt.web.utils.SimulatorConfiguration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Consumer;

@WebServlet(name = "SimulatorConfigServlet", urlPatterns = "/simulatorConfig")
public class SimulatorConfigServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int maxMoves = Integer.parseInt(req.getParameter("maxMoves"));
        Simulator simulator = new Simulator(maxMoves);

        SimulatorConfiguration simulatorConfig = new SimulatorConfiguration(simulator);

        configureParameter(req, "gridHeight", simulatorConfig::setGridHeight);
        configureParameter(req, "gridWidth", simulatorConfig::setGridWidth);
        configureParameter(req, "numberOfGrids", simulatorConfig::setNumberOfGrids);
        configureParameter(req, "numberOfBottles", simulatorConfig::setNumberOfBottles);

        String variableBottlesParam = req.getParameter("variableBottles");
        if (variableBottlesParam != null) {
            boolean variableBottles = Boolean.parseBoolean(variableBottlesParam);
            simulatorConfig.setVariableBottles(variableBottles);
        }

        req.getSession().setAttribute("simulatorConfig", simulatorConfig);
    }

    private static void configureParameter(HttpServletRequest request, String parameterName, Consumer<Integer> setter) {
        String paramString = request.getParameter(parameterName);
        if (paramString == null) return;

        try {
            int parameter = Integer.parseInt(paramString);
            setter.accept(parameter);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
