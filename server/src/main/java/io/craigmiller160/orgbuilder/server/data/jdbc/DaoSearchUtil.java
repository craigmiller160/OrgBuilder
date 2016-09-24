package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.DTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craigmiller on 9/24/16.
 */
public class DaoSearchUtil<T extends DTO> {

    public List<T> search(SearchQuery searchQuery, String elementName, Connection connection,
                                          ResultSetParser<T> resultSetParsing) throws OrgApiDataException{
        List<T> results = new ArrayList<>();
        OrgApiLogger.getDataLogger().trace(elementName + " Search Query: " + searchQuery.getQuery());
        try(PreparedStatement stmt = searchQuery.createAndParameterizeStatement(connection)){
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    results.add(resultSetParsing.parseResultSet(resultSet));
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to perform search query", ex);
        }

        return results;
    }

    public interface ResultSetParser<T extends DTO>{

        T parseResultSet(ResultSet resultSet) throws SQLException;

    }

}
