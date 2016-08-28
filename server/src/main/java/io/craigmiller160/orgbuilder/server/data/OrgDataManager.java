package io.craigmiller160.orgbuilder.server.data;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.data.jdbc.JdbcDataConnection;
import io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager;
import io.craigmiller160.orgbuilder.server.data.jdbc.SchemaManager;

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
        this.schemaManager = new SchemaManager(dataSource, jdbcManager);
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

    public DataConnection connectToSchema(String schemaName) throws OrgApiDataException{
        if(!schemaManager.schemaExists(schemaName)){
            throw new OrgApiDataException("Schema does not exist. Schema Name: " + schemaName);
        }

        return new JdbcDataConnection(dataSource, jdbcManager, schemaName);
    }

    public void createDefaultAppSchema() throws OrgApiDataException{
        createSchema(SchemaManager.DEFAULT_APP_SCHEMA_NAME, true, false);
    }

    public void createAppSchema(String schemaName) throws OrgApiDataException{
        createSchema(schemaName, true, true);
    }

    public void createOrgSchema(String schemaName) throws OrgApiDataException{
        createSchema(schemaName, false, true);
    }

    private void createSchema(String schemaName, boolean isAppSchema, boolean failIfExists) throws OrgApiDataException{
        if(schemaManager.schemaExists(schemaName)){
            if(failIfExists){
                throw new OrgApiDataException("Schema already exists. Schema Name: " + schemaName);
            }
            return;
        }
        schemaManager.createSchema(schemaName, isAppSchema);
    }

    public void deleteSchema(String schemaName) throws OrgApiDataException{
        schemaManager.deleteSchema(schemaName);
    }

}
