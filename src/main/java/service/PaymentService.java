package service;

import dao.abstraction.GenericAccountDao;
import dao.abstraction.PaymentDao;
import dao.factory.DaoFactory;
import dao.config.HibernateUtil;
import entity.Account;
import entity.Payment;
import entity.User;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer. Implements operations of finding,
 * creating, deleting entities. Payment dao layer.
 *
 * @author JohnUkraine
 */
public class PaymentService {
  private final DaoFactory daoFactory = DaoFactory.getInstance();
  private final Session session = HibernateUtil.getInstance();

  private PaymentService() {}

  private static class Singleton {
    private final static PaymentService INSTANCE = new PaymentService();
  }

  public static PaymentService getInstance() {
    return Singleton.INSTANCE;
  }

  public Optional<Payment> findById(Long id) {
    PaymentDao paymentDao = daoFactory.getPaymentDao(session);
    return paymentDao.findOne(id);
  }

  public List<Payment> findAll() {
    PaymentDao paymentDao = daoFactory.getPaymentDao(session);
    return paymentDao.findAll();
  }


  public List<Payment> findAllByUser(User user) {
    PaymentDao paymentDao = daoFactory.getPaymentDao(session);
    return paymentDao.findByUser(user);
  }

  public List<Payment> findAllByAccount(Long accountNumber) {
    PaymentDao paymentDao = daoFactory.getPaymentDao(session);
    return paymentDao.findByAccount(accountNumber);
  }

  public List<Payment> findAllByCard(Long cardNumber) {
    PaymentDao paymentDao = daoFactory.getPaymentDao(session);
    return paymentDao.findByCardNumber(cardNumber);
  }

  public Payment createPayment(Payment payment) {
    PaymentDao paymentDao = daoFactory.getPaymentDao(session);
    GenericAccountDao accountDaoFrom =
        daoFactory.getAccountDao(session, payment.getAccountFrom().getAccountType());
    GenericAccountDao accountDaoTo =
        daoFactory.getAccountDao(session, payment.getAccountTo().getAccountType());


    Account accountFrom = payment.getAccountFrom();
    Account accountTo = payment.getAccountTo();
    BigDecimal amount = payment.getAmount();

    session.beginTransaction();

    accountDaoFrom.decreaseBalance(accountFrom, amount);
    accountDaoTo.increaseBalance(accountTo, amount);

    Payment inserted = paymentDao.insert(payment);

    session.getTransaction().commit();

    return inserted;
  }

  @SuppressWarnings("unchecked")
  public Payment createPaymentWithUpdate(Payment payment) {
    PaymentDao paymentDao = daoFactory.getPaymentDao(session);

    GenericAccountDao accountDaoFrom =
        daoFactory.getAccountDao(session, payment.getAccountFrom().getAccountType());
    GenericAccountDao accountDaoTo =
        daoFactory.getAccountDao(session, payment.getAccountTo().getAccountType());


    Account accountFrom = payment.getAccountFrom();
    Account accountTo = payment.getAccountTo();
    BigDecimal amount = payment.getAmount();

    session.beginTransaction();

    accountDaoFrom.decreaseBalance(accountFrom, amount);
    accountDaoTo.increaseBalance(accountTo, amount);

    accountDaoFrom.update(accountFrom);
    accountDaoTo.update(accountTo);

    Payment inserted = paymentDao.insert(payment);

    session.getTransaction().commit();

    return inserted;
  }



}
