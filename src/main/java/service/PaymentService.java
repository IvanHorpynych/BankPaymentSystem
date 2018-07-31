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

  private PaymentService() {}

  private static class Singleton {
    private final static PaymentService INSTANCE = new PaymentService();
  }

  public static PaymentService getInstance() {
    return Singleton.INSTANCE;
  }

  public Optional<Payment> findById(Long id) {
    try(Session session = HibernateUtil.getInstance()) {
      PaymentDao paymentDao = daoFactory.getPaymentDao();
      return paymentDao.findOne(id);
    }
  }

  public List<Payment> findAll() {
    try(Session session = HibernateUtil.getInstance()) {
      PaymentDao paymentDao = daoFactory.getPaymentDao();
      return paymentDao.findAll();
    }
  }


  public List<Payment> findAllByUser(User user) {
    try(Session session = HibernateUtil.getInstance()) {
      PaymentDao paymentDao = daoFactory.getPaymentDao();
      return paymentDao.findByUser(user);
    }
  }

  public List<Payment> findAllByAccount(Long accountNumber) {
    try(Session session = HibernateUtil.getInstance()) {
      PaymentDao paymentDao = daoFactory.getPaymentDao();
      return paymentDao.findByAccount(accountNumber);
    }
  }

  public List<Payment> findAllByCard(Long cardNumber) {
    try(Session session = HibernateUtil.getInstance()) {
      PaymentDao paymentDao = daoFactory.getPaymentDao();
      return paymentDao.findByCardNumber(cardNumber);
    }
  }

  public Payment createPayment(Payment payment) {
    try(Session session = HibernateUtil.getInstance()) {
      PaymentDao paymentDao = daoFactory.getPaymentDao();
      GenericAccountDao accountDaoFrom =
              daoFactory.getAccountDao(payment.getAccountFrom().getAccountType());
      GenericAccountDao accountDaoTo =
              daoFactory.getAccountDao(payment.getAccountTo().getAccountType());


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
  }

  @SuppressWarnings("unchecked")
  public Payment createPaymentWithUpdate(Payment payment) {
    try(Session session = HibernateUtil.getInstance()) {
      PaymentDao paymentDao = daoFactory.getPaymentDao();

      GenericAccountDao accountDaoFrom =
              daoFactory.getAccountDao(payment.getAccountFrom().getAccountType());
      GenericAccountDao accountDaoTo =
              daoFactory.getAccountDao(payment.getAccountTo().getAccountType());


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

}
