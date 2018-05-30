package dao.abstraction;

import entity.Account;
import entity.Status;
import entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountsDao extends GenericAccountDao<Account>{

    Optional<Account> findOneByType(int typeId);

}
