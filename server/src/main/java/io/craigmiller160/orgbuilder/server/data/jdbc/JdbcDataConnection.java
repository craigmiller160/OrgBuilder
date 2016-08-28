package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by craig on 8/10/16.
 */
public class JdbcDataConnection implements DataConnection {

    private final Connection connection;
    private final JdbcManager jdbcManager;

    public JdbcDataConnection(OrgDataSource dataSource, JdbcManager jdbcManager, String schemaName) throws OrgApiDataException{
        this.jdbcManager = jdbcManager;
        try{
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            try(Statement stmt = connection.createStatement()){
                stmt.executeUpdate("use " + schemaName);
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to create new JDBC Data Transaction", ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> Dao<E, ?> newDao(Class<E> entityType) throws OrgApiDataException{
        Class<Dao<E,?>> daoClazz = (Class<Dao<E,?>>) jdbcManager.getEntityDaoMap().get(entityType);
        if(daoClazz != null){
            try {
                Constructor<Dao<E,?>> constructor = daoClazz.getConstructor(Connection.class);
                return constructor.newInstance(connection);
            }
            catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                throw new OrgApiDataException("Unable to instantiate Dao. Class: " + daoClazz.getName(), ex);
            }
        }

        return null;
    }

    @Override
    public void commit() throws OrgApiDataException {
        try{
            connection.commit();
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to commit database transaction", ex);
        }
    }

    @Override
    public void rollback() throws OrgApiDataException {
        try{
            connection.rollback();
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to rollback database transaction", ex);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
