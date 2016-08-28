package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.MemberJoins;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.JoinedWithMemberDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.Query;

/**
 * Created by craig on 8/23/16.
 */
public abstract class AbstractJdbcMemberJoinDao<E extends JoinedWithMemberDTO,I> extends AbstractJdbcDao<E,I> implements MemberJoins<E> {

    public AbstractJdbcMemberJoinDao(Connection connection, Map<Query,String> queries) {
        super(connection, queries);
    }

    protected abstract I getIdForElement(E element);

    @Override
    public E insert(E element) throws OrgApiDataException {
        E result = super.insert(element);
        if(result.isPreferred()){
            clearPreferred(result);
        }
        return result;
    }

    private void clearPreferred(E result) throws OrgApiDataException{
        I id = getIdForElement(result);
        try(PreparedStatement stmt = connection.prepareStatement(queries.get(Query.CLEAR_PREFERRED))){
            stmt.setObject(1, result.getMemberId());
            stmt.setObject(2, id);
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to reset " + getElementName() + " preferred field for other records after insert.", ex);
        }
    }

    @Override
    public E update(E element, I id) throws OrgApiDataException {
        E result =  super.update(element, id);
        if(result.isPreferred()){
            clearPreferred(result);
        }

        return result;
    }

    @Override
    public List<E> getAllByMember(long id) throws OrgApiDataException {
        String getAllByMemberQuery = queries.get(Query.GET_ALL_BY_MEMBER);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Get All By Member Query:\n" + getAllByMemberQuery);
        List<E> elements = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(getAllByMemberQuery)){
            stmt.setObject(1, id);
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    E element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get all " + getElementName() + " by member ID: " + id, ex);
        }
        return elements;
    }

    @Override
    public List<E> getAllByMember(long id, long offset, long size) throws OrgApiDataException {
        String getAllByMemberLimitQuery = queries.get(Query.GET_ALL_BY_MEMBER_LIMIT);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Get All By Member Limit Query:\n" + getAllByMemberLimitQuery);
        List<E> elements = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(getAllByMemberLimitQuery)){
            stmt.setObject(1, id);
            stmt.setLong(2, offset);
            stmt.setLong(3, size);
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    E element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get all " + getElementName() + " by member id, with limit. Member ID: " + id + " Offset: " + offset + " Size: " + size, ex);
        }
        return elements;
    }

    @Override
    public long getCountByMember(long id) throws OrgApiDataException {
        String getCountByMemberQuery = queries.get(Query.COUNT_BY_MEMBER);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Count By Member Query:\n" + getCountByMemberQuery);
        long count = -1;
        try(PreparedStatement stmt = connection.prepareStatement(getCountByMemberQuery)){
            stmt.setObject(1, id);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    count = resultSet.getLong(1);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get count of " + getElementName() + " by member id. Member ID: " + id, ex);
        }
        return count;
    }


}
