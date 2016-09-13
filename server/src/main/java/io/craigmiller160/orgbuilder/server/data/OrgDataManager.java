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
        return connectToSchema(null, schemaName);
    }

    public DataConnection connectToSchema(Connection connection, String schemaName) throws OrgApiDataException{
        connection = connection == null ? getConnection() : connection;
        if(!schemaManager.schemaExists(connection, schemaName)){
            throw new OrgApiDataException("Schema does not exist. Schema Name: " + schemaName);
        }

        return new JdbcDataConnection(dataSource, jdbcManager, schemaName);
    }

    public void createDefaultAppSchema() throws OrgApiDataException{
        createDefaultAppSchema(null);
    }

    public void createDefaultAppSchema(Connection connection) throws OrgApiDataException{
        createSchema(connection, SchemaManager.DEFAULT_APP_SCHEMA_NAME, true, false);
    }

    public void createAppSchema(String schemaName) throws OrgApiDataException{
        createAppSchema(null, schemaName);
    }

    public void createAppSchema(Connection connection, String schemaName) throws OrgApiDataException{
        createSchema(connection, schemaName, true, true);
    }

    public void createOrgSchema(String schemaName) throws OrgApiDataException{
        createOrgSchema(null, schemaName);
    }

    public void createOrgSchema(Connection connection, String schemaName) throws OrgApiDataException{
        createSchema(connection, schemaName, false, true);
    }

    private void createSchema(String schemaName, boolean isAppSchema, boolean failIfExists) throws OrgApiDataException{
        createSchema(null, schemaName, isAppSchema, failIfExists);
    }

    private void createSchema(Connection connection, String schemaName, boolean isAppSchema, boolean failIfExists) throws OrgApiDataException{
        connection = connection == null ? getConnection() : connection;
        if(schemaManager.schemaExists(connection, schemaName)){
            if(failIfExists){
                throw new OrgApiDataException("Schema already exists. Schema Name: " + schemaName);
            }
            return;
        }
        schemaManager.createSchema(connection, schemaName, isAppSchema);
    }

    public void deleteSchema(String schemaName) throws OrgApiDataException{
        deleteSchema(null, schemaName);
    }

    public void deleteSchema(Connection connection, String schemaName) throws OrgApiDataException{
        connection = connection == null ? getConnection() : connection;
        schemaManager.deleteSchema(connection, schemaName);
    }

}
