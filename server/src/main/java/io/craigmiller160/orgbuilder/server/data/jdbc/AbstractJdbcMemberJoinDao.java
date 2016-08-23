package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.MemberJoins;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 8/23/16.
 */
public abstract class AbstractJdbcMemberJoinDao<E,I,M> extends AbstractJdbcDao<E,I> implements MemberJoins<E,M> {

    public AbstractJdbcMemberJoinDao(Connection connection) {
        super(connection);
    }

    protected List<E> executeGetAllByMember(M id, String getAllByMemberQuery) throws OrgApiDataException{
        OrgApiLogger.getDataLogger().trace(getElementName() + " Get All By Member Query:\n" + getAllByMemberQuery);
        List<E> elements = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(getAllByMemberQuery)){
            stmt.setObject(1, id);
            try(ResultSet resultSet = stmt.executeQuery()){
                E element = parseResult(resultSet);
                elements.add(element);
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get all " + getElementName() + " by member ID: " + id, ex);
        }
        return elements;
    }

    protected List<E> executeGetAllByMemberLimit(M id, long offset, long size, String getAllByMemberLimitQuery) throws OrgApiDataException{
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

    protected long executeGetCountByMember(M id, String getCountByMemberQuery) throws OrgApiDataException{
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
