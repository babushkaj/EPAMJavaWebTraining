package by.training.hospital.listener;

import by.training.hospital.context.SecurityContext;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
public class HttpSessionAttributeListener implements javax.servlet.http.HttpSessionAttributeListener {

    private static final Logger LOGGER = Logger.getLogger(HttpSessionAttributeListener.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (event.getName().equals("currentUserId")) {
            Long currentUserIdAttr = (Long) event.getValue();
            SecurityContext.getInstance().addNewUserSession(currentUserIdAttr, event.getSession());
            LOGGER.info("User with ID = " + currentUserIdAttr + " entered the site.");
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if (event.getName().equals("currentUserId")) {
            LOGGER.info("User with ID = " + event.getValue() + " left the site.");
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {

    }
}
