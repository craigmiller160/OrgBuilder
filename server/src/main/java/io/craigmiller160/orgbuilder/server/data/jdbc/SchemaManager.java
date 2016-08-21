package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;

import java.sql.*;

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
                String query = String.format(CREATE_SCHEMA_SQL, schemaName);
                stmt.executeUpdate(query);
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to create schema. Schema Name: " + schemaName, ex);
        }
    }

}
