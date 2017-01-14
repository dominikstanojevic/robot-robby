package hr.fer.zemris.projekt.web.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "TrainingFilter", urlPatterns = {"/train"})
public class TrainingFilter implements Filter {

    private static boolean isRunning = false;
    private static final Object trainingLock = new Object();

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //content type must be set to text/event-stream
        resp.setContentType("text/event-stream");

        //encoding must be set to UTF-8
        resp.setCharacterEncoding("UTF-8");

        synchronized (trainingLock) {
            if (isRunning) {
                System.out.println("Training already running.");
                resp.getWriter().write("data: finished\n\n");
                resp.getWriter().flush();
                return;
            }

            isRunning = true;
        }

        try {
            chain.doFilter(req, resp);
        } finally {
            isRunning = false;
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
