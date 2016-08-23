package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.AbstractDao;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craigmiller on 8/17/16.
 */
public abstract class AbstractJdbcDao<E,I> extends AbstractDao<E,I> {

    private final Connection connection;

    protected AbstractJdbcDao(Connection connection){
        this.connection = connection;
    }

    protected Connection getConnection(){
        return connection;
    }

    protected abstract void parameterizeElement(PreparedStatement stmt, E element) throws SQLException;

    protected abstract E parseResult(ResultSet resultSet) throws SQLException;

    protected E executeInsert(E element, String insertQuery) throws OrgApiDataException{
        String simpleName = element.getClass().getSimpleName();
        OrgApiLogger.getDataLogger().trace(simpleName + " Insert Query:\n" + insertQuery);
        try{
            I id = null;
            try(PreparedStatement stmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)){
                parameterizeElement(stmt, element);
                stmt.executeUpdate();
                try(ResultSet resultSet = stmt.getGeneratedKeys()){
                    if(!resultSet.next()){
                        throw new SQLException("Unable to retrieve ID from inserted Address. Address: " + element.toString());
                    }
                    //noinspection unchecked
                    id = (I) resultSet.getObject(1);
                }
            }

            element = get(id);
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to insert " + simpleName + ": " + element.toString(), ex);
        }

        return element;
    }

    protected E executeUpdate(E element, I id, int updateKeyParamIndex, String updateQuery) throws OrgApiDataException{
        String simpleName = element.getClass().getSimpleName();
        OrgApiLogger.getDataLogger().trace(simpleName + " Update Query:\n" + updateQuery);
        try(PreparedStatement stmt = connection.prepareStatement(updateQuery)){
            parameterizeElement(stmt, element);
            stmt.setObject(updateKeyParamIndex, id);
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to update " + simpleName + ":" + element.toString(), ex);
        }

        return element;
    }

    protected E executeDelete(I id, String deleteQuery) throws OrgApiDataException{
        E element = get(id);
        String simpleName = element.getClass().getSimpleName();
        try{
            OrgApiLogger.getDataLogger().trace(simpleName + " Delete Query:\n" + deleteQuery);
            try(PreparedStatement stmt = connection.prepareStatement(deleteQuery)){
                stmt.setObject(1, id);
                stmt.executeUpdate();
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to delete " + simpleName + ". ID: " + id, ex);
        }

        return element;
    }

    protected E executeGet(I id, String elementName, String getQuery) throws OrgApiDataException{
        OrgApiLogger.getDataLogger().trace(elementName + " Get By ID Query:\n" + getQuery);
        E element = null;
        try(PreparedStatement stmt = connection.prepareStatement(getQuery)){
            stmt.setObject(1, id);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    element = parseResult(resultSet);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve " + elementName + " by ID: " + id, ex);
        }

        return element;
    }

    protected long executeCount(String elementName, String countQuery) throws OrgApiDataException{
        OrgApiLogger.getDataLogger().trace(elementName + " Count Query:\n" + countQuery);
        long count = -1;
        try(Statement stmt = connection.createStatement()){
            try(ResultSet resultSet = stmt.executeQuery(countQuery)){
                if(resultSet.next()){
                    count = resultSet.getLong(1);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve count of " + elementName, ex);
        }

        return count;
    }

    protected List<E> executeGetAll(String elementName, String getAllQuery) throws OrgApiDataException{
        OrgApiLogger.getDataLogger().trace(elementName + " Get All Query:\n" + getAllQuery);
        List<E> elements = new ArrayList<>();
        try(Statement stmt = connection.createStatement()){
            try(ResultSet resultSet = stmt.executeQuery(getAllQuery)){
                while(resultSet.next()){
                    E element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve all " + elementName, ex);
        }

        return elements;
    }

    protected List<E> executeGetAllLimit(String elementName, long offset, long size, String getAllLimitQuery) throws OrgApiDataException{
        OrgApiLogger.getDataLogger().trace(elementName + " Get All Limit Query:\n" + getAllLimitQuery);
        List<E> elements = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(getAllLimitQuery)){
            stmt.setLong(1, offset);
            stmt.setLong(2, size);
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    E element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve " + elementName + " within range. Offset: " + offset + " Size: " + size, ex);
        }

        return elements;
    }

}
