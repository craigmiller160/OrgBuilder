package io.craigmiller160.orgbuilder.server.data;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

/**
 * Created by craig on 8/10/16.
 */
public class DaoFactoryProvider {

    public static DaoFactory newDefaultFactory(){
        String defaultFactory = ServerCore.getProperty(ServerProps.DEFAULT_DAO_PROP);
        return newFactory(defaultFactory);
    }

    public static DaoFactory newFactory(String className) {
        DaoFactory daoFactory = null;
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz != null && clazz.isAssignableFrom(DaoFactory.class)) {
                daoFactory = (DaoFactory) clazz.newInstance();
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            OrgApiLogger.getDataLogger().error("Unable to instantiate DaoFactory. Class: " + className, ex);
        }

        return daoFactory;
    }

}
