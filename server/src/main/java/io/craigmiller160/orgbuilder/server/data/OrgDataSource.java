package io.craigmiller160.orgbuilder.server.data;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by craig on 8/20/16.
 */
public class OrgDataSource {

    private final BasicDataSource dataSource;

    public OrgDataSource(){
        dataSource = new BasicDataSource();
        String clazz = ServerCore.getProperty(ServerProps.DB_CLASS_PROP);
        String url = ServerCore.getProperty(ServerProps.DB_URL_PROP);
        String user = ServerCore.getProperty(ServerProps.DB_USER_PROP);
        String pass = ServerCore.getProperty(ServerProps.DB_PASS_PROP);
        String initString = ServerCore.getProperty(ServerProps.POOL_INIT_SIZE_PROP);
        String maxString = ServerCore.getProperty(ServerProps.POOL_MAX_SIZE_PROP);
        int init = StringUtils.isNumeric(initString) ? Integer.parseInt(initString) : -1;
        int max = StringUtils.isNumeric(maxString) ? Integer.parseInt(maxString) : -1;

        dataSource.setDriverClassName(clazz);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(pass);
        if(init >= 0){
            dataSource.setInitialSize(init);
        }

        if(max > 0){
            dataSource.setMaxTotal(max);
            dataSource.setMaxIdle(max);
        }
    }

    public synchronized Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public synchronized void closeDataSource() throws SQLException{
        dataSource.close();
    }

}
