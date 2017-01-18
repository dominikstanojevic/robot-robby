package hr.fer.zemris.projekt.web.servlets.play;

import com.google.gson.Gson;
import hr.fer.zemris.projekt.grid.Grid;

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
@WebServlet("/newGrid")
public class NewGridServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson GSON = new Gson();

        Grid grid = new Grid();
        grid.generate(10, 10, 50, false);

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(GSON.toJson(grid));
        resp.getWriter().flush();
    }
}
