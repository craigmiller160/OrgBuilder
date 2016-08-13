package io.craigmiller160.orgbuilder.server.data;

import java.util.List;

/**
 * The public API of all DAO objects in this application.
 * This interface defines methods that handle all key CRUD
 * operations.
 *
 * In order to support more flexible implementations, it
 * also contains the query() method. Implementation classes
 * will likely have a wide range of possible operations to
 * perform. Rather than referencing those implementations
 * directly, the query() method can be used to call into those
 * other methods. The implementing classes will determine how
 * this is done, however all methods must return a List<E>
 * to match the query() method's return type.
 *
 * Created by craig on 8/10/16.
 *
 * @param <E> the type of the element this DAO persists.
 * @param <I> the type of the primary key, or ID, of the
 *           element this DAO persists.
 */
public interface Dao<E,I> {

    /**
     * Insert the provided element into the database.
     *
     * @param element the element to be inserted.
     * @return the element, after being inserted.
     */
    E insert(E element);

    /**
     * Update the provided element in the database.
     *
     * @param element the element to update.
     * @return the element, after being updated.
     */
    E update(E element);

    /**
     * Delete the element with the provided ID from
     * the database.
     *
     * @param id the ID of the element.
     * @return the element that was deleted.
     */
    E delete(I id);

    /**
     * Get the element with the provided ID from
     * the database.
     *
     * @param id the ID of the element.
     * @return the element that was retrieved.
     */
    E get(I id);

    /**
     * Get the element with the provided ID
     * from the database. If other elements are joined
     * with it, they will be retrieved along with it.
     *
     * @param id the ID of the element.
     * @return the element that was retrieved.
     */
    E getWithJoins(I id);

    /**
     * Get the count of the total number of
     * elements of the type managed by this
     * DAO in the database.
     *
     * @return the count of the elements of
     *          this type in the database.
     */
    int getCount();

    /**
     * Get all of the elements of the type
     * managed by this DAO in the database.
     *
     * @return all of the elements of this type in
     *          the database.
     */
    List<E> getAll();

    /**
     * Get all of the elements of the type
     * managed by this DAO in the database.
     * If other elements are joined
     * with it, they will be retrieved along with it.
     *
     * @return all of the elements of this type in
     *          the database, with all elements
     *          joined with them.
     */
    List<E> getAllWithJoins();

    /**
     * Get all of the elements of the type
     * managed by this DAO in the database,
     * limited by the offset and limit parameters.
     *
     * @param offset the offset to start getting elements at.
     * @param limit the limit of how many elements to retrieve.
     * @return all of the elements of this type between the
     *          offset and limit in the database.
     */
    List<E> getAll(int offset, int limit);

    /**
     * Get all of the elements of the type
     * managed by this DAO in the database,
     * limited by the offset and limit parameters.
     * If other elements are joined
     * with it, they will be retrieved along with it.
     *
     * @param offset the offset to start getting elements at.
     * @param limit the limit of how many elements to retrieve.
     * @return all of the elements of this type between the
     *          offset and limit in the database, with all
     *          elements joined with them.
     */
    List<E> getAllWithJoins(int offset, int limit);

    /**
     * Invoke an additional query method in the
     * implementation of this interface, that matches
     * the provided name and parameters.
     *
     * @param queryName the name of the query method.
     * @param params the parameters for the query method.
     * @return a list of the element results.
     */
    List<E> query(String queryName, Object...params);

}
