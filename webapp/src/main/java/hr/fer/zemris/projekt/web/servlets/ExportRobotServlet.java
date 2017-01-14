package hr.fer.zemris.projekt.web.servlets;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.Robot;

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

@WebServlet(name = "ExportRobotServlet", urlPatterns = {"/exportRobot"})
public class ExportRobotServlet extends HttpServlet {

    public static final int BUFFER_SIZE = 4096;
    private static int counter = 0;
    private static final Object exportLock = new Object();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Robot robot = (Robot) req.getSession().getAttribute(Constants.SESSION_KEY_ROBOT);
        Algorithm algorithm = (Algorithm) req.getSession().getAttribute(Constants.SESSION_KEY_ALGORITHM);

        Path filePath;
        synchronized (exportLock) {
            filePath = Paths.get("exportFile" + counter);
            counter++;
        }

        algorithm.writeSolutionToFile(filePath, robot);

        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) Files.size(filePath));
        resp.setHeader(
                "Content-Disposition",
                String.format("attachment; filename=\"%s\"", createFileName(robot, algorithm))
        );

        ServletOutputStream os = resp.getOutputStream();
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(filePath))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;

            while ((length = bis.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
        os.flush();

        Files.delete(filePath);
    }

    private static String createFileName(Robot robot, Algorithm algorithm) {
        StringBuilder sb = new StringBuilder();

        sb.append(algorithm.toString());
        sb.append("_");

        String fitness = String.valueOf(robot.standardizedFitness());
        sb.append(fitness.substring(fitness.indexOf(".") + 1));

        sb.append(".robby");

        return sb.toString();
    }
}
