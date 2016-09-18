package io.craigmiller160.orgbuilder.server.data;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.data.jdbc.JdbcDataConnection;
import io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager;
import io.craigmiller160.orgbuilder.server.data.jdbc.SchemaManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by craig on 8/10/16.
 */
public class OrgDataManager {

    private final OrgDataSource dataSource;
    private final SchemaManager schemaManager;
    private final JdbcManager jdbcManager;

    public OrgDataManager(OrgDataSource dataSource) throws OrgApiException{
        this.dataSource = dataSource;
        this.jdbcManager = JdbcManager.newInstance();
        this.schemaManager = new SchemaManager(jdbcManager);
    }

    OrgDataSource getDataSource(){
        return dataSource;
    }

    SchemaManager getSchemaManager(){
        return schemaManager;
    }

    JdbcManager getJdbcManager(){
        return jdbcManager;
    }

    public DataConnection connectToAppSchema() throws OrgApiDataException{
        return connectToSchema(SchemaManager.DEFAULT_APP_SCHEMA_NAME);
    }

    private Connection getConnection() throws OrgApiDataException{
        try{
            return dataSource.getConnection();
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to open database connection", ex);
        }
    }

    public DataConnection connectToSchema(String schemaName) throws OrgApiDataException{
        return new JdbcDataConnection(getConnection(), schemaManager, jdbcManager, schemaName);
    }

    public void createDefaultAppSchema() throws OrgApiDataException{
        createSchema(SchemaManager.DEFAULT_APP_SCHEMA_NAME, true, false);
    }

    public void createAppSchema(String schemaName) throws OrgApiDataException{
        createSchema(schemaName, true, false);
    }

    public void createOrgSchema(String schemaName) throws OrgApiDataException{
        createSchema(schemaName, false, true);
    }

    public void createSchema(String schemaName, boolean isAppSchema, boolean failIfExists) throws OrgApiDataException{
        try(Connection connection = getConnection()){
            schemaManager.createSchema(connection, schemaName, isAppSchema, failIfExists);
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Failed open database connection to create schema", ex);
        }
    }

    public void deleteSchema(String schemaName, boolean failIfNotExists) throws OrgApiDataException{
        try(Connection connection = getConnection()){
            schemaManager.deleteSchema(connection, schemaName, failIfNotExists);
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Failed open database connection to delete schema", ex);
        }
    }

}
