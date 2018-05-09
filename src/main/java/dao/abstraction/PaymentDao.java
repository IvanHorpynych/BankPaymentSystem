package dao.abstraction;

import entity.Account;
import entity.Payment;
import entity.User;

import java.util.List;

public interface PaymentDao extends GenericDao<Payment, Integer> {

    /**
     * Retrieves all payments associated with certain account.
     *
     * @param account account to retrieve payments
     * @return list of retrieved payments
     */
    List<Payment> findByAccount(Account account);

    /**
     * Retrieves all payments associated with certain user.
     *
     * @param user user to retrieve payments
     * @return list of retrieved payments
     */
    List<Payment> findByUser(User user);

}
