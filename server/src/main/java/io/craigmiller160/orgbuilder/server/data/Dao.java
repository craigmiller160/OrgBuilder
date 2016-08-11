package io.craigmiller160.orgbuilder.server.data;

import java.util.List;

/**
 * Created by craig on 8/10/16.
 */
public interface Dao<E,I> {

    void insert(E element);

    void update(E element);

    void delete(I id);

    E get(I id, boolean withJoins);

    int getCount();

    List<E> getAll(boolean withJoins);

    List<E> getAll(boolean withJoins, int offset, int limit);

    List<E> query(String queryName, Object...params);

}
