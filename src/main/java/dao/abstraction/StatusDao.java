package dao.abstraction;

import entity.Status;

import java.util.Optional;

public interface StatusDao extends GenericDao<Status, Integer> {

    /**
     * Retrieve status from database identified by name.
     * @param name identifier of status
     * @return optional, which contains retrieved object or null
     */
    Optional<Status> findOneByName(String name);
}
