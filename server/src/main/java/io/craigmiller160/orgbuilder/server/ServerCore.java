package io.craigmiller160.orgbuilder.server;

import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by craig on 8/13/16.
 */
public class ServerCore implements ServletContextListener{

    private static final String PROPS_PATH = "io/craigmiller160/orgbuilder/server/orgapi.properties";
    private static final Properties properties = new Properties();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try{
            OrgApiLogger.getServerLogger().debug("Loading API application properties");
            properties.load(getClass().getClassLoader().getResourceAsStream(PROPS_PATH));
        }
        catch(IOException ex){
            OrgApiLogger.getServerLogger().error("Unable to load API application properties", ex);
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
