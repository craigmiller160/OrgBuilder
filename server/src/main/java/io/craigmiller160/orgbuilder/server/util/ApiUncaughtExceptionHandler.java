package io.craigmiller160.orgbuilder.server.util;

import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

/**
 * The default UncaughtExceptionHandler for this application.
 *
 * Created by craig on 8/12/16.
 */
public class ApiUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        OrgApiLogger.getUtilLogger().error("Uncaught Exception! Thread ID: " + t.getId() + " Name: " + t.getName(), e);
    }
}
