package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.data.DataDTOMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import throwing.stream.ThrowingStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by craig on 8/27/16.
 */
public class JdbcManager {

    private static final String QUERY_KEY = "QUERY=";
    private static final String SQL_FILE_PATH = "io/craigmiller160/orgbuilder/server/data/mysql/";
    private static final String QUERY_FILE_SUFFIX = "_queries.sql";
    private static final String SCHEMA_FILE_SUFFIX = "_ddl.sql";

    private final Map<Class<? extends Dao>,Map<Query,String>> mappedQueries;
    private final Map<Schema,List<String>> schemaScripts;

    public static JdbcManager newInstance() throws OrgApiQueryParsingException{
        return new JdbcManager();
    }

    private JdbcManager() throws OrgApiQueryParsingException{
        OrgApiLogger.getDataLogger().debug("Initializing JdbcManager");
        Map<Class<? extends Dao>,Map<Query,String>> mappedQueries = new HashMap<>();
        Collection<DataDTOMap> dataDTOMaps = ServerCore.getDataDTOMap().values();
        ThrowingStream.of(dataDTOMaps.stream(), OrgApiQueryParsingException.class)
                .forEach((ddm) -> mappedQueries.put(ddm.getDaoType(), (Map<Query,String>) parseDaoQueries(createQueryFileName(ddm.getDaoType()), true)));

        Map<Schema,List<String>> schemaScripts = new HashMap<>();
        schemaScripts.put(Schema.APP_SCHEMA, (List<String>) parseDaoQueries(createSchemaFileName(Schema.APP_SCHEMA), false));
        schemaScripts.put(Schema.ORG_SCHEMA, (List<String>) parseDaoQueries(createSchemaFileName(Schema.ORG_SCHEMA), false));

        this.mappedQueries = Collections.unmodifiableMap(mappedQueries);
        this.schemaScripts = Collections.unmodifiableMap(schemaScripts);
    }

    public Map<Class<? extends Dao>,Map<Query,String>> getMappedQueries(){
        return mappedQueries;
    }

    public Map<Schema,List<String>> getSchemaScripts(){
        return schemaScripts;
    }

    private String createQueryFileName(Class<? extends Dao> clazz){
        return String.format("%s%s%s", SQL_FILE_PATH, clazz.getSimpleName(), QUERY_FILE_SUFFIX);
    }

    private String createSchemaFileName(Schema schema){
        return String.format("%s%s%s", SQL_FILE_PATH, schema.toString(), SCHEMA_FILE_SUFFIX);
    }

    private Object parseDaoQueries(String file, boolean isNamedQueries) throws OrgApiQueryParsingException{
        Object queries = isNamedQueries ? new HashMap<Query,String>() : new ArrayList<String>();
        OrgApiLogger.getDataLogger().trace("Attempting to load sql file. File: " + file + " Named Queries: " + isNamedQueries);

        InputStream fileStream = getClass().getClassLoader().getResourceAsStream(file);
        StrBuilder queryBuilder = new StrBuilder();
        int currentLine = 0;
        String delimiter = ";";
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream))){
            Query currentQuery = null;
            String line = null;
            while((line = reader.readLine()) != null){
                currentLine++;
                //If the line is blank, and nothing has been added to the queryBuilder yet, skip it
                if(StringUtils.isEmpty(line) && queryBuilder.length() == 0) {
                    continue;
                }
                //If the line is a comment, it only matters if it's the header for a named query.
                else if(line.trim().startsWith("-- ")){
                    if(isNamedQueries && line.trim().length() > 3 && line.trim().substring(3).startsWith(QUERY_KEY)){
                        String newQuery = line.trim().substring(9);
                        if(currentQuery != null){
                            throw new IOException("New named query is declared while prior query is being parsed. " +
                                    "Current Query: " + currentQuery.toString() + " New Query: " + newQuery);
                        }
                        currentQuery = Query.valueOf(newQuery);
                    }
                    continue;
                }
                //If the line starts with "delimiter", change the current delimiter value
                else if(line.trim().toLowerCase().startsWith("delimiter")){
                    String[] lineSplit = StringUtils.split(line);
                    delimiter = lineSplit[lineSplit.length - 1];
                    continue;
                }

                boolean endQuery = false;
                if(line.trim().endsWith(delimiter)){
                    endQuery = true;
                    line = line.replace(delimiter, "");
                }

                queryBuilder.appendln(line);
                if(endQuery){
                    if(isNamedQueries){
                        if(currentQuery == null){
                            throw new IOException("No name found for parsed named query");
                        }
                        ((Map<Query,String>)queries).put(currentQuery, queryBuilder.toString());
                        currentQuery = null;
                    }
                    else{
                        ((List<String>) queries).add(queryBuilder.toString());
                    }

                    queryBuilder.clear();
                }
            }
        }
        catch(IOException ex){
            throw new OrgApiQueryParsingException("Error parsing query file. File: " + file + " Line: " + currentLine, ex);
        }

        OrgApiLogger.getDataLogger().debug("Queries loaded from file. File: " + file);

        if(isNamedQueries){
            queries = Collections.unmodifiableMap((Map<Query,String>) queries);
        }

        return queries;
    }

    public enum Query{
        INSERT,
        UPDATE,
        DELETE,
        GET_BY_ID,
        COUNT,
        GET_ALL,
        GET_ALL_LIMIT,
        GET_ALL_BY_MEMBER,
        GET_ALL_BY_MEMBER_LIMIT,
        COUNT_BY_MEMBER,
        CLEAR_PREFERRED,
        GET_PREFERRED_FOR_MEMBER,
        INSERT_OR_UPDATE,
        DELETE_BY_MEMBER,
        GET_WITH_NAME,
        GET_BY_ID_AND_MEMBER;
    }

    public enum Schema {
        ORG_SCHEMA,
        APP_SCHEMA;
    }

}
