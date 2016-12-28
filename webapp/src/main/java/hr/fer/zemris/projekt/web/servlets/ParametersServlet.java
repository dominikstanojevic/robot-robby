package hr.fer.zemris.projekt.web.servlets;

import com.google.gson.Gson;
import hr.fer.zemris.projekt.Algorithms;
import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.Parameters;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet(name = "ParametersServlet", urlPatterns = {"/parameters"})
public class ParametersServlet extends HttpServlet {

    private static final Gson GSON = new Gson();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String algorithmID = req.getParameter("algorithmID");

        Parameters<? extends Algorithm> parameters = Algorithms.getDefaultParameters(algorithmID);
        Set<Parameter> parameterSet = parameters.getParameters();

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(GSON.toJson(parameterSet));
        resp.getWriter().flush();
    }
}
