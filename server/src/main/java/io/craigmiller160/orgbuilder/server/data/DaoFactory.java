package io.craigmiller160.orgbuilder.server.data;

/**
 * Created by craig on 8/10/16.
 */
public interface DaoFactory {

    <E,I> Dao<E,I> newDao(Class<Dao<E,I>> clazz);

}
