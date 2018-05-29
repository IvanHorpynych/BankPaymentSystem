package dao.abstraction;

import java.util.List;
import java.util.Optional;

/**
 * Common interface for all dao.
 *
 * @param <T>   represents type of domain object
 * @param <ID>  represents type of identifier
 */
public interface GenericDao <T, ID>{
    /**
     * Retrieves object from database identified by id.
     *
     * @param id    identifier of domain object.
     * @return      optional, which contains retrieved object or null
     */
    Optional<T> findOne(ID id);

    /**
     * Retrieves all object data from database.
     *
     * @return List of objects which represent one row in database.
     */
    List<T> findAll();

    /**
     * Insert object to a database.
     *
     * @param obj object to insert
     * @return inserted object
     */
    T insert(T obj);

    /**
     * Update object's information in database.
     *
     * @param obj object to update
     */
    @SuppressWarnings("unchecked")
    void update(T obj);

    /**
     * Delete certain object, identified by id, from database.
     *
     * @param id identifier of the object.
     */
    void delete(ID id);

    /**
     * Check object's existing in database.
     *
     * @param id identifier of the object.
     * @return {@code true} if exists else {@code false}
     */
    default boolean exist(ID id) {
        return findOne(id).isPresent();
    }

}
