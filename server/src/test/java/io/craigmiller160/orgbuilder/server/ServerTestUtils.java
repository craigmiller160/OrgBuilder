package io.craigmiller160.orgbuilder.server;

import io.craigmiller160.orgbuilder.server.data.OrgDataManager;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import io.craigmiller160.orgbuilder.server.data.mysql.JdbcManager;

import java.lang.reflect.Method;

/**
 * Created by craig on 8/21/16.
 */
public class ServerTestUtils {

    public static OrgDataSource getOrgDataSource(OrgDataManager dataManager) throws Exception{
        return (OrgDataSource) executeMethod(dataManager.getClass(), dataManager, "getDataSource");
    }

    public static JdbcManager getJdbcManager(OrgDataManager dataManager) throws Exception{
        return (JdbcManager) executeMethod(dataManager.getClass(), dataManager, "getJdbcManager");
    }

    private static Object executeMethod(Class<?> clazz, Object instance, String methodSig) throws Exception{
        Method m = clazz.getDeclaredMethod(methodSig);
        m.setAccessible(true);
        return m.invoke(instance);
    }

}
