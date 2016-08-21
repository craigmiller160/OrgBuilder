package io.craigmiller160.orgbuilder.server.data;

import io.craigmiller160.orgbuilder.server.data.jdbc.JdbcDataConnection;
import io.craigmiller160.orgbuilder.server.data.jdbc.SchemaManager;

/**
 * Created by craig on 8/10/16.
 */
public class OrgDataManager {

    private final OrgDataSource dataSource;
    private final SchemaManager schemaManager;

    public OrgDataManager(OrgDataSource dataSource){
        this.dataSource = dataSource;
        this.schemaManager = new SchemaManager(dataSource);
    }

    OrgDataSource getDataSource(){
        return dataSource;
    }

    SchemaManager getSchemaManager(){
        return schemaManager;
    }

    public DataConnection connectToSchema(String schemaName) throws OrgApiDataException{
        if(!schemaManager.schemaExists(schemaName)){
            throw new OrgApiDataException("Schema does not exist. Schema Name: " + schemaName);
        }

        return new JdbcDataConnection(dataSource, schemaName);
    }

    public void createNewSchema(String schemaName) throws OrgApiDataException{
        if(schemaManager.schemaExists(schemaName)){
            throw new OrgApiDataException("Schema already exists. Schema Name: " + schemaName);
        }
        schemaManager.createSchema(schemaName);
    }



}
