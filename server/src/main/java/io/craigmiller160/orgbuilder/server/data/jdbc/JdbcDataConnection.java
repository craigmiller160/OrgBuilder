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

    private static final Map<Class,Class> entityDaoMap;

    //Static initializer to populate the daoMap
    static {
        Map<Class,Class> map = new HashMap<>();
        map.put(AddressDTO.class, AddressDao.class);
        map.put(MemberDTO.class, MemberDao.class);
        map.put(OrgDTO.class, OrgDao.class);

        entityDaoMap = Collections.unmodifiableMap(map);
    }

    private final Connection connection;

    public JdbcDataConnection(OrgDataSource dataSource, String schemaName) throws OrgApiDataException{
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

    @Override
    public <E> Dao<E, ?> newDao(Class<E> entityType) throws OrgApiDataException{
        Class<Dao<E,?>> daoClazz = entityDaoMap.get(entityType);
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
        //TODO finish this
    }

    @Override
    public void rollback() throws OrgApiDataException {
        //TODO finish this
    }
}
