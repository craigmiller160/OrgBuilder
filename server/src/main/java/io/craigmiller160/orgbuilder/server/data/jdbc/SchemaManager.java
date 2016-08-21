package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 8/21/16.
 */
public class SchemaManager {

    private static final String SCHEMA_EXISTS_SQL =
            "select schema_name " +
            "from information_schema.schemata " +
            "where schema_name = ?;";

    private static final String CREATE_SCHEMA_SQL =
            "create schema %1$s;";

    private static final String USE_SCHEMA_SQL =
            "use %1$s;";

    private static final String SHOW_TABLES_SQL =
            "show tables from %1$s;";

    private final OrgDataSource dataSource;

    public SchemaManager(OrgDataSource dataSource){
        this.dataSource = dataSource;
    }

    public boolean schemaExists(String schemaName) throws OrgApiDataException{
        try(Connection conn = dataSource.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(SCHEMA_EXISTS_SQL)){
                stmt.setString(1, schemaName);
                try(ResultSet resultSet = stmt.executeQuery()){
                    return resultSet.next();
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to check if schema exists. Schema Name: " + schemaName, ex);
        }
    }

    public void createSchema(String schemaName) throws OrgApiDataException{
        try(Connection conn = dataSource.getConnection()){
            try(Statement stmt = conn.createStatement()){
                String createSchemaQuery = String.format(CREATE_SCHEMA_SQL, schemaName);
                String useSchemaQuery = String.format(USE_SCHEMA_SQL, schemaName);

                stmt.executeUpdate(createSchemaQuery);
                stmt.executeUpdate(useSchemaQuery);

                String[] ddlScript = ServerCore.getDDLScript();
                for(String query : ddlScript){
                    stmt.executeUpdate(query);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to create schema. Schema Name: " + schemaName, ex);
        }
    }

    String[] getTableNames(String schemaName) throws OrgApiDataException{
        List<String> tableNames = new ArrayList<>();
        try(Connection conn = dataSource.getConnection()){
            try(Statement stmt = conn.createStatement()){
                String query = String.format(SHOW_TABLES_SQL, schemaName);
                try(ResultSet resultSet = stmt.executeQuery(query)){
                    while(resultSet.next()){
                        tableNames.add(resultSet.getString(1));
                    }
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve all table names for schema. Schema Name: " + schemaName, ex);
        }

        return tableNames.toArray(new String[tableNames.size()]);
    }

}
