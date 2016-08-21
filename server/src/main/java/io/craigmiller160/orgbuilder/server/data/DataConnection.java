package io.craigmiller160.orgbuilder.server.data;

/**
 * Created by craig on 8/10/16.
 */
public interface DataConnection {

    <E> Dao<E,?> newDao(Class<E> entityType) throws OrgApiDataException;

    void commit() throws OrgApiDataException;

}
