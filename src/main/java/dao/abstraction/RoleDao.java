package dao.abstraction;

import entity.Role;

import java.util.Optional;

public interface RoleDao extends GenericDao<Role, Integer> {

    /**
     * Retrieve role from database identified by name.
     * @param name identifier of status
     * @return optional, which contains retrieved object or null
     */
    Optional<Role> findOneByName(String name);
}
