package by.training.hospital.listener;

import by.training.hospital.context.ApplicationContext;
import by.training.hospital.context.ContextException;
import by.training.hospital.context.SecurityContext;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(ApplicationContextListener.class);

    private static final String APP_CONTEXT_INIT = "ApplicationContext has been initialized.";
    private static final String APP_CONTEXT_DESTROY = "ApplicationContext has been destroyed.";
    private static final String APP_CONTEXT_INIT_ERROR = "Error during ApplicationContext initialization.";
    private static final String APP_CONTEXT_DESTROY_ERROR = "Error during ApplicationContext destroying.";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            ApplicationContext.getInstance().init();
            SecurityContext.getInstance().init();
            LOGGER.info(APP_CONTEXT_INIT);
        } catch (ContextException e) {
            LOGGER.error(APP_CONTEXT_INIT_ERROR, e);
            throw new RuntimeException("Error during context initialization.", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ApplicationContext.getInstance().destroy();
            LOGGER.info(APP_CONTEXT_DESTROY);
        } catch (ContextException e) {
            LOGGER.error(APP_CONTEXT_DESTROY_ERROR, e);
            throw new RuntimeException("Error during context destroying.", e);
        }
    }
}
