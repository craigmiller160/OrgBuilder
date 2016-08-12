package io.craigmiller160.orgbuilder.server.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper class for the loggers used by this
 * application. This class provided several pre-configured
 * loggers to be used across the application.
 *
 * Created by craig on 8/11/16.
 */
public class OrgApiLogger {

    /*
     * The logger category names
     */
    private static final String RESOURCE_LOGGER = "io.craigmiller160.orgbuilder.server.resource";
    private static final String SERVICE_LOGGER = "io.craigmiller160.orgbuilder.server.service";
    private static final String DATA_LOGGER = "io.craigmiller160.orgbuilder.server.data";
    private static final String UTIL_LOGGER = "io.craigmiller160.orgbuilder.server.util";

    /*
     * The loggers
     */
    private static final Logger resourceLogger;
    private static final Logger serviceLogger;
    private static final Logger dataLogger;
    private static final Logger utilLogger;

    //Initialize the loggers
    static {
        resourceLogger = LoggerFactory.getLogger(RESOURCE_LOGGER);
        serviceLogger = LoggerFactory.getLogger(SERVICE_LOGGER);
        dataLogger = LoggerFactory.getLogger(DATA_LOGGER);
        utilLogger = LoggerFactory.getLogger(UTIL_LOGGER);
    }

    /**
     * Get the logger for RESTful resource classes.
     *
     * @return the logger.
     */
    public static Logger getResourceLogger(){
        return resourceLogger;
    }

    /**
     * Get the logger for service classes.
     *
     * @return the logger.
     */
    public static Logger getServiceLogger(){
        return serviceLogger;
    }

    /**
     * Get the logger for data classes.
     *
     * @return the logger.
     */
    public static Logger getDataLogger(){
        return dataLogger;
    }

    /**
     * Get the logger for utility classes.
     *
     * @return the logger.
     */
    public static Logger getUtilLogger(){
        return utilLogger;
    }

}
