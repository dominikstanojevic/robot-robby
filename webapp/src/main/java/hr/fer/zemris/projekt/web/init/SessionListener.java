package hr.fer.zemris.projekt.web.init;

import hr.fer.zemris.projekt.web.servlets.Constants;
import hr.fer.zemris.projekt.web.utils.SimulatorConfiguration;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        SimulatorConfiguration configuration = new SimulatorConfiguration();

        httpSessionEvent.getSession().setAttribute(Constants.SESSION_KEY_SIMULATOR_CONFIG, configuration);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().removeAttribute(Constants.SESSION_KEY_SIMULATOR_CONFIG);
    }
}
