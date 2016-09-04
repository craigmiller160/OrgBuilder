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
    private static final String REST_LOGGER = "io.craigmiller160.orgbuilder.server.rest";
    private static final String SERVICE_LOGGER = "io.craigmiller160.orgbuilder.server.service";
    private static final String DATA_LOGGER = "io.craigmiller160.orgbuilder.server.data";
    private static final String UTIL_LOGGER = "io.craigmiller160.orgbuilder.server.util";
    private static final String SERVER_LOGGER = "io.craigmiller160.orgbuilder.server";

    /*
     * The loggers
     */
    private static final Logger restLogger;
    private static final Logger serviceLogger;
    private static final Logger dataLogger;
    private static final Logger utilLogger;
    private static final Logger serverLogger;

    //Initialize the loggers
    static {
        restLogger = LoggerFactory.getLogger(REST_LOGGER);
        serviceLogger = LoggerFactory.getLogger(SERVICE_LOGGER);
        dataLogger = LoggerFactory.getLogger(DATA_LOGGER);
        utilLogger = LoggerFactory.getLogger(UTIL_LOGGER);
        serverLogger = LoggerFactory.getLogger(SERVER_LOGGER);
    }

    /**
     * Get the logger for RESTful rest classes.
     *
     * @return the logger.
     */
    public static Logger getRestLogger(){
        return restLogger;
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

    /**
     * Get the logger for server classes.
     *
     * @return the logger.
     */
    public static Logger getServerLogger(){
        return serverLogger;
    }

}
