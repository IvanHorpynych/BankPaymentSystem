package dao.abstraction;

import entity.Account;
import entity.Payment;
import entity.User;

import java.util.List;

public interface PaymentDao extends GenericDao<Payment, Long> {

    /**
     * Retrieves all payments associated with certain account.
     *
     * @param accountNumber account number to retrieve payments
     * @return list of retrieved payments
     */
    List<Payment> findByAccount(Long accountNumber);

    /**
     * Retrieves all payments associated with certain user.
     *
     * @param user user to retrieve payments
     * @return list of retrieved payments
     */
    List<Payment> findByUser(User user);

    /**
     * Retrieves all payments associated with certain user.
     *
     * @param cardNumber card number to retrieve payments
     * @return list of retrieved payments
     */
    List<Payment> findByCardNumber(long cardNumber);

}
