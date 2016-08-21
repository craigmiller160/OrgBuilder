package io.craigmiller160.orgbuilder.server;

import io.craigmiller160.orgbuilder.server.data.OrgDataManager;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.util.ApiUncaughtExceptionHandler;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Created by craig on 8/13/16.
 */
public class ServerCore implements ServletContextListener{

    private static final String PROPS_PATH = "io/craigmiller160/orgbuilder/server/orgapi.properties";
    private static final String DDL_PATH = "io/craigmiller160/orgbuilder/server/data/jdbc/org_schema_ddl.sql";

    private static final Properties properties = new Properties();
    private static OrgDataManager orgDataManager;
    private static String ddlScript;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try{
            OrgApiLogger.getServerLogger().info("Initializing API ServerCore");

            Thread.setDefaultUncaughtExceptionHandler(new ApiUncaughtExceptionHandler());
            OrgApiLogger.getServerLogger().debug("Loading API application properties");
            properties.load(getClass().getClassLoader().getResourceAsStream(PROPS_PATH));

            OrgApiLogger.getServerLogger().debug("Configuration database utilities");
            OrgDataSource dataSource = new OrgDataSource();
            orgDataManager = new OrgDataManager(dataSource);

            OrgApiLogger.getServerLogger().debug("Loading DDL script into memory");
            ddlScript = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(DDL_PATH), Charset.defaultCharset());

        }
        catch(IOException ex){
            OrgApiLogger.getServerLogger().error("Unable to load API application properties", ex);
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    public static OrgDataManager getOrgDataManager(){
        return orgDataManager;
    }

    public static String getDDLScript(){
        return ddlScript;
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
