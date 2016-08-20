package io.craigmiller160.orgbuilder.server;

import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.util.ApiUncaughtExceptionHandler;

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
    private static OrgDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try{
            OrgApiLogger.getServerLogger().info("Initializing API ServerCore");

            Thread.setDefaultUncaughtExceptionHandler(new ApiUncaughtExceptionHandler());
            OrgApiLogger.getServerLogger().debug("Loading API application properties");
            properties.load(getClass().getClassLoader().getResourceAsStream(PROPS_PATH));

            OrgApiLogger.getServerLogger().debug("Configuring data source");
            dataSource = new OrgDataSource();
        }
        catch(IOException ex){
            OrgApiLogger.getServerLogger().error("Unable to load API application properties", ex);
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    public static OrgDataSource getDataSource(){
        return dataSource;
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
