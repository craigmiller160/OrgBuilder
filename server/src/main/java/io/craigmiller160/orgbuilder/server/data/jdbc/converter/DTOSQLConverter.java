package io.craigmiller160.orgbuilder.server.data.jdbc.converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by craig on 8/28/16.
 */
public interface DTOSQLConverter<E> {

    void parameterizeElement(PreparedStatement stmt, E element) throws SQLException;

    E parseResultSet(ResultSet resultSet) throws SQLException;

    int getUpdateKeyParamIndex();

}
