package io.craigmiller160.orgbuilder.server.data.dbutils;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DaoFactory;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by craig on 8/10/16.
 */
public class DbUtilsDaoFactory implements DaoFactory {

    private static final Map<Class,Class> entityDaoMap;

    //Static initializer to populate the daoMap
    static {
        Map<Class,Class> map = new HashMap<>();
        map.put(MemberDTO.class, MemberDao.class);

        entityDaoMap = Collections.unmodifiableMap(map);
    }

    @Override
    public <E> Dao<E, ?> newDao(Class<E> entityType) {
        Class<Dao<E,?>> daoClazz = entityDaoMap.get(entityType);
        if(daoClazz != null){
            try {
                return daoClazz.newInstance();
            }
            catch (InstantiationException | IllegalAccessException ex) {
                OrgApiLogger.getDataLogger().error("Unable to instantiate Dao. Class: " + daoClazz.getName(), ex);
            }
        }

        return null;
    }
}
