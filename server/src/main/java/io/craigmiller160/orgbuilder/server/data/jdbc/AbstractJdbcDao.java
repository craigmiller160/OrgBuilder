package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.AbstractDao;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;

import java.sql.Connection;

/**
 * Created by craigmiller on 8/17/16.
 */
public abstract class AbstractJdbcDao<E,I> extends AbstractDao<E,I> {

//    private final OrgDataSource dataSource;

    //TODO if this field is accessed concurrently, it will need synchronization
    private Connection sharedConnection;

    protected AbstractJdbcDao(){
//        this.dataSource = ServerCore.getDataSource();
    }

//    public OrgDataSource getDataSource(){
//        return dataSource;
//    }
//
//    public void setSharedConnection(Connection sharedConnection){
//        this.sharedConnection = sharedConnection;
//    }
//
//    public boolean isUsingSharedConnection(){
//        return this.sharedConnection != null;
//    }

    protected E executeUpdate(){
        //TODO finish this
        return null;
    }

    protected E executeQuery(){
        //TODO finish this
        return null;
    }

    protected E executeBatch(){
        //TODO finish this
        return null;
    }

}
