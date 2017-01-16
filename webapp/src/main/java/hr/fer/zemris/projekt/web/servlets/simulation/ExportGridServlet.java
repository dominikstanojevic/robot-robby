package hr.fer.zemris.projekt.web.servlets.simulation;

import hr.fer.zemris.projekt.grid.Grid;
import hr.fer.zemris.projekt.web.servlets.Constants;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/exportGrid")
public class ExportGridServlet extends HttpServlet {

    private static final int BUFFER_SIZE = 4096;
    private static int counter = 0;
    private static final Object exportLock = new Object();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Grid grid = (Grid) req.getSession().getAttribute(Constants.SESSION_KEY_GRID);

        Path filePath;
        synchronized (exportLock) {
            filePath = Paths.get("gridFile" + counter);
            counter++;
        }

        grid.writeToFile(filePath);

        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) Files.size(filePath));
        resp.setHeader(
                "Content-Disposition",
                "attachment; filename=\"map.grid\""
        );

        ServletOutputStream os = resp.getOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int length;
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(filePath))) {

            while ((length = bis.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
        os.flush();

        Files.delete(filePath);
    }

}
