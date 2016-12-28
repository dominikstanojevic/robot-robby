package hr.fer.zemris.projekt.web.init;

import hr.fer.zemris.projekt.web.utils.SimulatorConfiguration;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        SimulatorConfiguration configuration = new SimulatorConfiguration();

        httpSessionEvent.getSession().setAttribute("simulatorConfig", configuration);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().removeAttribute("simulatorConfig");
    }
}
