package hr.fer.zemris.projekt.web.servlets;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "GetRobotServlet", urlPatterns = {"/getRobot"})
public class GetRobotServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Robot robot = (Robot) req.getSession().getAttribute(Constants.SESSION_KEY_ROBOT);
        Algorithm algorithm = (Algorithm) req.getSession().getAttribute(Constants.SESSION_KEY_ALGORITHM);

        if (robot == null || algorithm == null) {
            resp.setStatus(400);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fitness", robot.standardizedFitness());
        jsonObject.put("algorithm", algorithm.toString());

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(jsonObject.toString());
        resp.getWriter().flush();
    }
}
