package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.AbstractDao;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;

import java.sql.Connection;

/**
 * Created by craigmiller on 8/17/16.
 */
public abstract class AbstractJdbcDao<E,I> extends AbstractDao<E,I> {

    private final Connection connection;

    protected AbstractJdbcDao(Connection connection){
        this.connection = connection;
    }

    protected Connection getConnection(){
        return connection;
    }

}
