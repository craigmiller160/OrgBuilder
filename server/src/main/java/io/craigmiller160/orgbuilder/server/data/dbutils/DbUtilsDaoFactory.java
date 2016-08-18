package io.craigmiller160.orgbuilder.server.data.dbutils;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DaoFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

    private static class OrgDataSource {

        private final BasicDataSource dataSource;

        public OrgDataSource(){
            dataSource = new BasicDataSource();
            String clazz = ServerCore.properties.getProperty(ServerProps.DB_CLASS_PROP);
            String url = ServerCore.properties.getProperty(ServerProps.DB_URL_PROP);
            String user = ServerCore.properties.getProperty(ServerProps.DB_USER_PROP);
            String pass = ServerCore.properties.getProperty(ServerProps.DB_PASS_PROP);
            String initString = ServerCore.properties.getProperty(ServerProps.POOL_INIT_SIZE_PROP);
            String maxString = ServerCore.properties.getProperty(ServerProps.POOL_MAX_SIZE_PROP);
            int init = StringUtils.isNumeric(initString) ? Integer.parseInt(initString) : -1;
            int max = StringUtils.isNumeric(maxString) ? Integer.parseInt(maxString) : -1;

            dataSource.setDriverClassName(clazz);
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(pass);
            if(init >= 0){
                dataSource.setInitialSize(init);
            }

            if(max >= 0){
                dataSource.setMaxTotal(max);
            }
        }

        public Connection getConnection() throws SQLException {
            return dataSource.getConnection();
        }

    }
}
