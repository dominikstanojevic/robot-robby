package hr.fer.zemris.projekt.web.servlets.simulation;

import hr.fer.zemris.projekt.grid.Grid;
import hr.fer.zemris.projekt.web.servlets.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
@MultipartConfig
@WebServlet("/generateGrid")
public class GenerateGridServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int width = Integer.parseInt(req.getParameter("width"));
        int height = Integer.parseInt(req.getParameter("height"));
        double bottlePercentage = Double.parseDouble(req.getParameter("percentage"));
        bottlePercentage /= 100;

        int bottleCount = (int)(width * height * bottlePercentage);

        Grid grid = new Grid();
        grid.generate(width, height, bottleCount, false);

        req.getSession().setAttribute(Constants.SESSION_KEY_GRID, grid);
    }
}
