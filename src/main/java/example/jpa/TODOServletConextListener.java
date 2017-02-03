
package example.jpa;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class TODOServletConextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(final ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(final ServletContextEvent arg0) {
        final TODOListResource tlr = new TODOListResource();
        //tlr.populateDB();
    }

}
