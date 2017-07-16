package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.AbstractDao;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.dto.DTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.Query;

/**
 * Created by craigmiller on 8/17/16.
 */
public abstract class AbstractJdbcDao<E extends DTO<I>,I> extends AbstractDao<E,I>  {

    protected final Connection connection;
    protected final Map<Query,String> queries;
    protected final DTOSQLConverter<E> converter;

    protected AbstractJdbcDao(Connection connection, Map<Query,String> queries){
        this.connection = connection;
        this.queries = queries;
        this.converter = getDTOSQLConverter();
    }

    protected abstract String getElementName();

    protected abstract DTOSQLConverter<E> getDTOSQLConverter();

    protected E parseResultSetAdditional(E element, ResultSet resultSet) throws SQLException{
        //Do nothing, for subclasses to provide extra implementation
        return element;
    }

    @Override
    public E insert(E element) throws OrgApiDataException {
        String insertQuery = queries.get(Query.INSERT);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Insert Query:\n" + insertQuery);
        try{
            return executeInsert(element, insertQuery, false);
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to insert " + getElementName() + ": " + element.toString(), ex);
        }
    }

    //This method can be used for insert or insertOrUpdate
    private E executeInsert(E element, String query, boolean orUpdate) throws SQLException, OrgApiDataException{
        I id = null;
        try(PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            converter.parameterizeElement(stmt, element);
            int result = stmt.executeUpdate();
            //If result is 0, nothing was changed, return null
            if(result == 0){
                return null;
            }

            try(ResultSet resultSet = stmt.getGeneratedKeys()){
                if(resultSet.next()){
                    //Get only the first ID, if this is an InsertOrUpdate that did an Update, there will be two values, the first is what is needed
                    //noinspection unchecked
                    id = (I) resultSet.getObject(1);
                }
                else if(orUpdate){
                    //If InsertOrUpdate and ResultSet had nothing, try getting the ID from the element that was passed in to be updated
                    //This is because if the updated element didn't change, no value will be returned here
                    id = element.getElementId();
                }
            }
        }

        if(id == null){
            //If the ID is null, then it wasn't able to be retrieved and an exception should be thrown
            throw new SQLException("Unable to retrieve ID from inserted " + getElementName() + ": " + element.toString());
        }

        return get(id);
    }

    @Override
    public E insertOrUpdate(E element) throws OrgApiDataException {
        String insertOrUpdateQuery = queries.get(Query.INSERT_OR_UPDATE);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Insert or Update Query:\n" + insertOrUpdateQuery);
        try{
            return executeInsert(element, insertOrUpdateQuery, true);
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to insert or update " + getElementName() + ": " + element.toString(), ex);
        }

    }

    @Override
    public E update(E element, I id) throws OrgApiDataException {
        String updateQuery = queries.get(Query.UPDATE);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Update Query:\n" + updateQuery);
        try(PreparedStatement stmt = connection.prepareStatement(updateQuery)){
            converter.parameterizeElement(stmt, element);
            stmt.setObject(converter.getUpdateKeyParamIndex(), id);
            int result = stmt.executeUpdate();
            //If the update returned 0, that means nothing was updated, and nothing should be returned
            if(result == 0){
                return null;
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to update " + getElementName() + ":" + element.toString(), ex);
        }

        //Otherwise, get the fully updated value from the database and return it
        return get(id);
    }

    @Override
    public E delete(I id) throws OrgApiDataException {
        String deleteQuery = queries.get(Query.DELETE);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Delete Query:\n" + deleteQuery);
        E element = get(id);
        try{
            try(PreparedStatement stmt = connection.prepareStatement(deleteQuery)){
                stmt.setObject(1, id);
                int result = stmt.executeUpdate();
                //If result is 0, nothing was changed, return null
                if(result == 0){
                    return null;
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to delete " + getElementName() + ". ID: " + id, ex);
        }

        return element;
    }

    @Override
    public E get(I id) throws OrgApiDataException {
        String getQuery = queries.get(Query.GET_BY_ID);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Get By ID Query:\n" + getQuery);
        E element = null;
        try(PreparedStatement stmt = connection.prepareStatement(getQuery)){
            stmt.setObject(1, id);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    element = parseResultSet(resultSet);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve " + getElementName() + " by ID: " + id, ex);
        }

        return element;
    }

    @Override
    public long getCount() throws OrgApiDataException {
        String countQuery = queries.get(Query.COUNT);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Count Query:\n" + countQuery);
        long count = -1;
        try(Statement stmt = connection.createStatement()){
            try(ResultSet resultSet = stmt.executeQuery(countQuery)){
                if(resultSet.next()){
                    count = resultSet.getLong(1);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve count of " + getElementName(), ex);
        }

        return count;
    }

    @Override
    public List<E> getAll() throws OrgApiDataException {
        String getAllQuery = queries.get(Query.GET_ALL);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Get All Query:\n" + getAllQuery);
        List<E> elements = new ArrayList<>();
        try(Statement stmt = connection.createStatement()){
            try(ResultSet resultSet = stmt.executeQuery(getAllQuery)){
                while(resultSet.next()){
                    elements.add(parseResultSet(resultSet));
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve all " + getElementName(), ex);
        }

        return elements;
    }

    protected E parseResultSet(ResultSet resultSet) throws SQLException{
        E element = converter.parseResultSet(resultSet);
        return parseResultSetAdditional(element, resultSet);
    }

    @Override
    public List<E> getAll(long offset, long size) throws OrgApiDataException {
        String getAllLimitQuery = queries.get(Query.GET_ALL_LIMIT);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Get All Limit Query:\n" + getAllLimitQuery);
        List<E> elements = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(getAllLimitQuery)){
            stmt.setLong(1, offset);
            stmt.setLong(2, size);
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    elements.add(parseResultSet(resultSet));
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve " + getElementName() + " within range. Offset: " + offset + " Size: " + size, ex);
        }

        return elements;
    }

}
