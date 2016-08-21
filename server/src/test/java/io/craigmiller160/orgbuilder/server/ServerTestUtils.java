package io.craigmiller160.orgbuilder.server;

import io.craigmiller160.orgbuilder.server.data.OrgDataManager;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;

import java.lang.reflect.Method;

/**
 * Created by craig on 8/21/16.
 */
public class ServerTestUtils {

    public static OrgDataSource getOrgDataSource(OrgDataManager dataManager) throws Exception{
        Class<?> clazz = dataManager.getClass();
        Method m = clazz.getDeclaredMethod("getDataSource");
        m.setAccessible(true);
        return (OrgDataSource) m.invoke(ServerCore.getOrgDataManager());
    }

}
