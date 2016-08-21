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

    public DataConnection connectToSchema(String schemaName) throws OrgApiDataException{
        return new JdbcDataConnection(dataSource);
    }

    public SchemaManager getSchemaManager(){
        return schemaManager;
    }

}
