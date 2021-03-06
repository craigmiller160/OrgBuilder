package io.craigmiller160.orgbuilder.server;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.OrgDataManager;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.rest.JWTUtil;
import io.craigmiller160.orgbuilder.server.util.ApiUncaughtExceptionHandler;
import io.craigmiller160.orgbuilder.server.data.DataDTOMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by craig on 8/13/16.
 */
public class ServerCore implements ServletContextListener{

    private static final String PROPS_PATH = "io/craigmiller160/orgbuilder/server/orgapi.properties";

    private static Map<Class,DataDTOMap> dataDtoMap;
    private static final Properties properties = new Properties();
    private static OrgDataManager orgDataManager;
    private static OrgDataSource orgDataSource;
    private static KeyManager keyManager;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try{
            OrgApiLogger.getServerLogger().info("Initializing API ServerCore");
            Thread.setDefaultUncaughtExceptionHandler(new ApiUncaughtExceptionHandler());

            try{
                OrgApiLogger.getServerLogger().debug("Loading API application properties");
                properties.load(getClass().getClassLoader().getResourceAsStream(PROPS_PATH));
            }
            catch(IOException ex){
                throw new OrgApiException("Unable to load API application properties", ex);
            }

            OrgApiLogger.getServerLogger().debug("Loading KeyStore");
            keyManager = new KeyManager();
            OrgApiLogger.getServerLogger().info("KeyStore loaded");

            try{
                OrgApiLogger.getServerLogger().debug("Configuring database utilities");
                dataDtoMap = DataDTOMap.generateDataDTOMap();
                orgDataSource = new OrgDataSource(properties);
                orgDataManager = new OrgDataManager(orgDataSource);
                orgDataManager.createDefaultAppSchema();
                OrgApiLogger.getServerLogger().info("Database utilities configured");
            }
            catch(OrgApiDataException ex){
                throw new OrgApiException("Unable to load and execute DDL scripts", ex);
            }
        }
        catch(OrgApiException ex){
            throw new RuntimeException("CRITICAL ERROR!!! Unable to properly initialize the SeverCore", ex);
        }
    }

    private String[] parseDDLScript(InputStream ddlStream) throws IOException{
        List<String> queries = new ArrayList<>();
        String delimiter = ";";
        StrBuilder queryBuilder = new StrBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(ddlStream))){
            String line = null;
            while((line = reader.readLine()) != null){
                //If the line is blank, and nothing has been added to the queryBuilder yet, skip it
                if(StringUtils.isEmpty(line) && queryBuilder.length() == 0) {
                    continue;
                }
                //If the line is a comment, skip it
                else if(line.trim().startsWith("--")){
                    continue;
                }
                //If the line starts with "delimiter", change the current delimiter value
                else if(line.trim().toLowerCase().startsWith("delimiter")){
                    String[] lineSplit = StringUtils.split(line);
                    delimiter = lineSplit[lineSplit.length - 1];
                    continue;
                }

                queryBuilder.appendln(line);
                if(line.trim().endsWith(delimiter)){
                    queries.add(queryBuilder.toString());
                    queryBuilder.clear();
                }
            }
        }
        return queries.toArray(new String[queries.size()]);
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    public static OrgDataManager getOrgDataManager(){
        return orgDataManager;
    }

    public static Map<Class,DataDTOMap> getDataDTOMap(){
        return dataDtoMap;
    }

    public static KeyManager getKeyManager(){
        return keyManager;
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if(orgDataSource != null){
            try {
                orgDataSource.closeDataSource();
            } catch (SQLException ex) {
                OrgApiLogger.getServerLogger().error("Failed to close OrgDataSource", ex);
            }
        }
    }
}
