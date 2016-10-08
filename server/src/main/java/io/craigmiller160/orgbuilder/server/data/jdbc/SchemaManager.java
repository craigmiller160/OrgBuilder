package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 8/21/16.
 */
public class SchemaManager {

    public static final String DEFAULT_APP_SCHEMA_NAME = "org_app";

    private static final String SCHEMA_EXISTS_SQL =
            "SELECT schema_name " +
            "FROM information_schema.schemata " +
            "WHERE schema_name = ?;";

    private static final String CREATE_SCHEMA_SQL =
            "create schema %1$s;";

    private static final String USE_SCHEMA_SQL =
            "use %1$s;";

    private static final String SHOW_TABLES_SQL =
            "show tables from %1$s;";

    private static final String DELETE_SCHEMA_SQL =
            "drop schema if exists %1$s;";

    private final JdbcManager jdbcManager;

    public SchemaManager(JdbcManager jdbcManager){
        this.jdbcManager = jdbcManager;
    }

    public boolean schemaExists(Connection connection, String schemaName) throws OrgApiDataException{
        try(PreparedStatement stmt = connection.prepareStatement(SCHEMA_EXISTS_SQL)){
            OrgApiLogger.getDataLogger().trace("Schema Exists Query:\n" + SCHEMA_EXISTS_SQL);
            stmt.setString(1, schemaName);
            try(ResultSet resultSet = stmt.executeQuery()){
                return resultSet.next();
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to check if schema exists. Schema Name: " + schemaName, ex);
        }
    }

    public void createSchema(Connection connection, String schemaName, boolean isAppSchema, boolean failIfExists) throws OrgApiDataException{
        OrgApiLogger.getDataLogger().debug("Trying to create schema. Is App Schema: " + isAppSchema + " | Schema Name: " + schemaName);
        if(schemaExists(connection, schemaName)){
            if(failIfExists){
                throw new OrgApiDataException("Error! Schema already exists. Is App Schema: " + isAppSchema + " | Schema Name: " + schemaName);
            }
            else{
                OrgApiLogger.getDataLogger().debug("Schema already exists, skipping creation. Is App Schema: " + isAppSchema + " | Schema Name: " + schemaName);
            }
            return;
        }

        try(Statement stmt = connection.createStatement()){
            String createSchemaQuery = String.format(CREATE_SCHEMA_SQL, schemaName);
            String useSchemaQuery = String.format(USE_SCHEMA_SQL, schemaName);
            OrgApiLogger.getDataLogger().trace("Create Schema Query:\n" + CREATE_SCHEMA_SQL);
            OrgApiLogger.getDataLogger().trace("Use Schema Query:\n" + USE_SCHEMA_SQL);

            stmt.executeUpdate(createSchemaQuery);
            stmt.executeUpdate(useSchemaQuery);

            List<String> schemaScript = isAppSchema ? jdbcManager.getSchemaScripts().get(JdbcManager.Schema.APP_SCHEMA) :
                    jdbcManager.getSchemaScripts().get(JdbcManager.Schema.ORG_SCHEMA);
            for(String query : schemaScript){
                stmt.executeUpdate(query);
            }

            OrgApiLogger.getDataLogger().info("Schema created successfully. Is App Schema: " + isAppSchema + " | Schema Name: " + schemaName);
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to create schema. Schema Name: " + schemaName, ex);
        }
    }

    public void deleteSchema(Connection connection, String schemaName, boolean failIfNotExists) throws OrgApiDataException{
        OrgApiLogger.getDataLogger().debug("Deleting schema. Schema Name: " + schemaName);
        if(!schemaExists(connection, schemaName)){
            if(failIfNotExists){
                throw new OrgApiDataException("Schema does not exist. Schema Name: " + schemaName);
            }
            return;
        }

        String query = String.format(DELETE_SCHEMA_SQL, schemaName);
        OrgApiLogger.getDataLogger().trace("Delete Schema Query:\n" + DELETE_SCHEMA_SQL);
        try(Statement stmt = connection.createStatement()){
            stmt.executeUpdate(query);
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to delete schema. Schema Name: " + schemaName, ex);
        }
    }

    String[] getTableNames(Connection connection, String schemaName) throws OrgApiDataException{
        List<String> tableNames = new ArrayList<>();
        try(Statement stmt = connection.createStatement()){
            String query = String.format(SHOW_TABLES_SQL, schemaName);
            OrgApiLogger.getDataLogger().trace("Show Schema Tables Query:\n" + DELETE_SCHEMA_SQL);
            try(ResultSet resultSet = stmt.executeQuery(query)){
                while(resultSet.next()){
                    tableNames.add(resultSet.getString(1));
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve all table names for schema. Schema Name: " + schemaName, ex);
        }

        return tableNames.toArray(new String[tableNames.size()]);
    }

}
