package io.craigmiller160.orgbuilder.server.data;

/**
 * Created by craig on 8/10/16.
 */
public interface DataConnection extends AutoCloseable{

    <E,I> Dao<E,I> newDao(Class<E> entityType) throws OrgApiDataException;

    void commit() throws OrgApiDataException;

    void rollback() throws OrgApiDataException;

    void close() throws OrgApiDataException;

}
