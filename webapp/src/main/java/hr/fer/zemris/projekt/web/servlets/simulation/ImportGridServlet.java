package hr.fer.zemris.projekt.web.servlets.simulation;

import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.grid.Grid;
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
@WebServlet("/importGrid")
public class ImportGridServlet extends HttpServlet {
    private static volatile int fileCounter = 0;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part gridFile = req.getPart("gridFile");

        InputStream is = gridFile.getInputStream();
        Path gridFilePath = Paths.get("grid" + (++fileCounter) + ".map");
        if (Files.exists(gridFilePath)){
            Files.delete(gridFilePath);
        }
        Files.createFile(gridFilePath);

        byte[] data = Utility.readStream(is);
        Files.write(gridFilePath, data);

        Grid grid = new Grid();
        grid.readFromFile(gridFilePath);
        req.getSession().setAttribute(Constants.SESSION_KEY_GRID, grid);
        Files.delete(gridFilePath);
    }
}
