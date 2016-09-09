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
     * @throws OrgApiDataException if unable to perform
     *                          the database operation.
     */
    E insert(E element) throws OrgApiDataException;

    /**
     * Update the provided element in the database.
     *
     * @param element the element to update.
     * @param id the ID of the element to update.
     * @return the element, after being updated.
     * @throws OrgApiDataException if unable to perform
     *                          the database operation.
     */
    E update(E element, I id) throws OrgApiDataException;

    /**
     * Insert or update the provided element, depending
     * on if it already exists in the database.
     *
     * @param element the element to insert or update.
     * @return the element, after insert or update.
     * @throws OrgApiDataException if unable to perform
     *                          the database operation.
     */
    E insertOrUpdate(E element) throws OrgApiDataException;

    /**
     * Delete the element with the provided ID from
     * the database.
     *
     * @param id the ID of the element.
     * @return the element that was deleted.
     * @throws OrgApiDataException if unable to perform
     *                          the database operation.
     */
    E delete(I id) throws OrgApiDataException;

    /**
     * Get the element with the provided ID from
     * the database.
     *
     * @param id the ID of the element.
     * @return the element that was retrieved.
     * @throws OrgApiDataException if unable to perform
     *                          the database operation.
     */
    E get(I id) throws OrgApiDataException;

    /**
     * Get the count of the total number of
     * elements of the type managed by this
     * DAO in the database.
     *
     * @return the count of the elements of
     *          this type in the database.
     * @throws OrgApiDataException if unable to perform
     *                          the database operation.
     */
    long getCount() throws OrgApiDataException;

    /**
     * Get all of the elements of the type
     * managed by this DAO in the database.
     *
     * @return all of the elements of this type in
     *          the database.
     * @throws OrgApiDataException if unable to perform
     *                          the database operation.
     */
    List<E> getAll() throws OrgApiDataException;

    /**
     * Get all of the elements of the type
     * managed by this DAO in the database,
     * limited by the offset and limit parameters.
     *
     * @param offset the offset to start getting elements at.
     * @param limit the limit of how many elements to retrieve.
     * @return all of the elements of this type between the
     *          offset and limit in the database.
     * @throws OrgApiDataException if unable to perform
     *                          the database operation.
     */
    List<E> getAll(long offset, long size) throws OrgApiDataException;

    /**
     * Invoke an additional query method in the
     * implementation of this interface, that matches
     * the provided name and parameters.
     *
     * @param queryName the name of the query method.
     * @param params the parameters for the query method.
     * @return a the results, based on the method called.
     * @throws OrgApiDataException if unable to perform
     *                          the database operation.
     */
    Object query(String queryName, Object...params) throws OrgApiDataException;

}
