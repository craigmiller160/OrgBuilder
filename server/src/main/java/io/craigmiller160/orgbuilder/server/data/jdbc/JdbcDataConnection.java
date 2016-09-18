package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by craig on 8/10/16.
 */
public class JdbcDataConnection implements DataConnection {

    private final Connection connection;
    private final JdbcManager jdbcManager;

    public JdbcDataConnection(Connection connection, SchemaManager schemaManager, JdbcManager jdbcManager, String schemaName) throws OrgApiDataException{
        this.jdbcManager = jdbcManager;
        this.connection = connection;
        try{
            if(!schemaManager.schemaExists(connection, schemaName)){
                throw new OrgApiDataException("Schema does not exist. Schema Name: " + schemaName);
            }
            connection.setAutoCommit(false);
            try(Statement stmt = connection.createStatement()){
                stmt.executeUpdate("use " + schemaName);
                connection.commit();
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to create new JDBC Data Transaction", ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E,I> Dao<E, I> newDao(Class<E> entityType) throws OrgApiDataException{
        Class<Dao<E,I>> daoClazz = (Class<Dao<E,I>>) ServerCore.getDataDTOMap().get(entityType).getDaoType();
        if(daoClazz != null){
            Map<JdbcManager.Query,String> queries = jdbcManager.getMappedQueries().get(daoClazz);
            try {
                Constructor<Dao<E,I>> constructor = daoClazz.getConstructor(Connection.class, Map.class);
                return constructor.newInstance(connection, queries);
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
    public void close() throws OrgApiDataException {
        try{
            connection.close();
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to close database connection", ex);
        }
    }
}
