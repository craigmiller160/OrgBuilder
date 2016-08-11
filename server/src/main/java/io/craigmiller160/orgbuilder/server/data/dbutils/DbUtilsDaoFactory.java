package io.craigmiller160.orgbuilder.server.data.dbutils;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DaoFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by craig on 8/10/16.
 */
public class DbUtilsDaoFactory implements DaoFactory {

    private static final Map<Class<Dao>,Dao> daoMap = new HashMap<>();

    //Static initializer to populate the daoMap
    static {

    }

    @Override
    public <E, I> Dao<E, I> newDao(Class<Dao<E, I>> clazz) {
        //noinspection unchecked
        return daoMap.get(clazz);
    }
}
