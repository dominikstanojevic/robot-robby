package hr.fer.zemris.projekt.web.servlets.simulation;

import com.google.gson.Gson;
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
@WebServlet("/createGrid")
public class CreateGridServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson GSON = new Gson();

        String gridJSON = req.getParameter("grid");
        Grid grid = GSON.fromJson(gridJSON, Grid.class);

        req.getSession().setAttribute(Constants.SESSION_KEY_GRID, grid);
    }
}
