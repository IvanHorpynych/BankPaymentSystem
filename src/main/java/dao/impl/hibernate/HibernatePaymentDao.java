package dao.impl.hibernate;

import dao.abstraction.PaymentDao;
import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class HibernatePaymentDao implements PaymentDao {

  private Session session;

  public HibernatePaymentDao(Session session) {
    this.session = session;
  }

  @Override
  public Optional<Payment> findOne(Long paymentId) {
    Query query = session.createQuery("from Payment where id = :paymentId", Payment.class);
    query.setParameter("paymentId", paymentId);
    query.setMaxResults(1);
    try {
      return Optional.ofNullable((Payment) query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }

  }

  @Override
  public List<Payment> findAll() {
    Query query = session.createQuery("from Payment ", Payment.class);
    return query.getResultList();
  }

  @Override
  public Payment insert(Payment payment) {
    Objects.requireNonNull(payment);
    session.beginTransaction();
    session.save(payment);
    session.getTransaction().commit();
    return payment;
  }

  @Override
  public void update(Payment payment) {
    Objects.requireNonNull(payment);
    Transaction transaction = session.beginTransaction();
    session.update(payment);
    transaction.commit();
  }

  @Override
  public void delete(Long paymentId) {
    Transaction transaction = session.beginTransaction();
    Payment payment = findOne(paymentId).get();
    session.delete(payment);
    transaction.commit();

  }

  @Override
  public List<Payment> findByAccount(Long accountNumber) {
    Query query = session.createQuery(
        "from Payment where accountFrom.id = :accountNumber or accountTo.id = :accountNumber",
        Payment.class);
    query.setParameter("accountNumber", accountNumber);
    query.setMaxResults(1);
    return query.getResultList();
  }

  @Override
  public List<Payment> findByUser(User user) {
    Objects.requireNonNull(user);
    Query query = session.createQuery(
        "from Payment where accountFrom.accountHolder.id = :accountHolder or accountTo.accountHolder.id = :accountHolder",
        Payment.class);
    query.setParameter("accountHolder", user.getId());
    return query.getResultList();
  }

  @Override
  public List<Payment> findByCardNumber(long cardNumber) {
    Query query =
        session.createQuery("from Payment where cardNumberFrom = :cardNumber", Payment.class);
    query.setParameter("cardNumber", cardNumber);
    query.setMaxResults(1);
    return query.getResultList();
  }


  /*
   * public static void main(String[] args) {
   * 
   * User user = User.newBuilder().addFirstName("first").addId(3).addLastName("last")
   * .addEmail("test@com").addPassword("123").addPhoneNumber("+123") .addRole(new
   * Role(Role.RoleIdentifier.USER_ROLE.getId(), "USER")).build();
   * 
   * CreditAccount creditAccount1 =
   * CreditAccount.newCreditBuilder().addAccountNumber(1).addAccountHolder(user) .addAccountType(new
   * AccountType(4, "CREDIT")).addBalance(BigDecimal.ONE)
   * .addCreditLimit(BigDecimal.TEN).addInterestRate(2L).addAccruedInterest(BigDecimal.ZERO)
   * .addValidityDate(new Date()).addStatus(new Status(1, "ACTIVE")).build();
   * 
   * CreditAccount creditAccount2 =
   * CreditAccount.newCreditBuilder().addAccountNumber(2).addAccountHolder(user) .addAccountType(new
   * AccountType(4, "CREDIT")).addBalance(BigDecimal.ONE)
   * .addCreditLimit(BigDecimal.TEN).addInterestRate(2L).addAccruedInterest(BigDecimal.ZERO)
   * .addValidityDate(new Date()).addStatus(new Status(1, "ACTIVE")).build();
   * 
   * HibernatePaymentDao mySqlPaymentDao = new HibernatePaymentDao();
   * 
   * for (Payment payment : mySqlPaymentDao.findAll()) { System.out.println(payment); }
   * 
   * System.out.println("Find one:"); System.out.println(mySqlPaymentDao.findOne(2L));
   * 
   * System.out.println("find dy user:"); System.out.println(mySqlPaymentDao.findByUser(user));
   * 
   * System.out.println("Find by account"); for (Payment payment :
   * mySqlPaymentDao.findByAccount(creditAccount1.getAccountNumber())) {
   * System.out.println(payment); }
   * 
   * System.out.println("Insert:"); Payment payment = mySqlPaymentDao
   * .insert(Payment.newBuilder().addAccountFrom(creditAccount1).addAccountTo(creditAccount2)
   * .addAmount(BigDecimal.TEN).addDate(new Date()).addCardNumberFrom(0L).build());
   * System.out.println("Find one:"); System.out.println(mySqlPaymentDao.findOne(payment.getId()));
   * 
   * for (Payment temp : mySqlPaymentDao.findAll()) { System.out.println(temp); }
   * System.out.println("update:");
   * 
   * payment.setAmount(BigDecimal.ONE); mySqlPaymentDao.update(payment);
   * 
   * System.out.println("Find one:"); System.out.println(mySqlPaymentDao.findOne(payment.getId()));
   * 
   * System.out.println("Delete"); mySqlPaymentDao.delete(payment.getId());
   * 
   * for (Payment temp : mySqlPaymentDao.findAll()) { System.out.println(temp); } }
   */

}