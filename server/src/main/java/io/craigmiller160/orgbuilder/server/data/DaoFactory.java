package io.craigmiller160.orgbuilder.server.data;

/**
 * Created by craig on 8/10/16.
 */
public interface DaoFactory {

    <E> Dao<E,?> newDao(Class<E> entityType);

}
