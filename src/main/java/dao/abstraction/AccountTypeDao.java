package dao.abstraction;

import entity.AccountType;

import java.util.Optional;

public interface AccountTypeDao extends GenericDao<AccountType, Integer> {

    /**
     * Retrieve type from database identified by name.
     * @param name identifier of status
     * @return optional, which contains retrieved object or null
     */
    Optional<AccountType> findOneByName(String name);
}
