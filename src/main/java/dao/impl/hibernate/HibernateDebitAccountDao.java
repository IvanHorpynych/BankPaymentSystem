
package dao.impl.hibernate;

import dao.abstraction.DebitAccountDao;
import dao.config.HibernateUtil;
import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class HibernateDebitAccountDao implements DebitAccountDao {


  @Override
  public Optional<Account> findOne(Long accountNumber) {
    Session session = HibernateUtil.getCurrentSession();
    Query query = session.createQuery(
        "from Account where accountNumber = :accountNumber and accountType.id = :typeId",
        Account.class);
    query.setParameter("accountNumber", accountNumber);
    query.setParameter("typeId", ((AccountType) session
        .createQuery("from AccountType where name = 'DEBIT'").getSingleResult()).getId());
    query.setMaxResults(1);
    try {
      return Optional.ofNullable((Account) query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }

  }

  @Override
  public List<Account> findAll() {
    Session session = HibernateUtil.getCurrentSession();
    Query query = session.createQuery("from Account where accountType.id = :typeId", Account.class);
    query.setParameter("typeId", ((AccountType) session
        .createQuery("from AccountType where name = 'DEBIT'").getSingleResult()).getId());
    return query.getResultList();
  }

  @Override
  public Account insert(Account account) {
    Session session = HibernateUtil.getCurrentSession();
    Objects.requireNonNull(account);
    session.save(account);
    return account;
  }

  @Override
  public void update(Account account) {
    Session session = HibernateUtil.getCurrentSession();
    Objects.requireNonNull(account);
    Transaction transaction = session.beginTransaction();
    session.update(account);
    transaction.commit();
  }

  @Override
  public void delete(Long accountNumber) {
    Session session = HibernateUtil.getCurrentSession();
    Transaction transaction = session.beginTransaction();
    Account account = findOne(accountNumber).get();
    session.delete(account);
    transaction.commit();

  }

  @Override
  public List<Account> findByUser(User user) {
    Session session = HibernateUtil.getCurrentSession();
    Objects.requireNonNull(user);
    Query query = session.createQuery(
        "from Account where accountHolder = :accountHolder and accountType.id = :typeId",
        Account.class);
    query.setParameter("accountHolder", user);
    query.setParameter("typeId", ((AccountType) session
        .createQuery("from AccountType where name = 'DEBIT'").getSingleResult()).getId());
    return query.getResultList();
  }

  @Override
  public List<Account> findAllNotClosed() {
    Session session = HibernateUtil.getCurrentSession();
    Query query = session.createQuery("from Account where status = :statusId", Account.class);
    query.setParameter("statusId",
        ((Status) session.createQuery("from Status where name != 'CLOSED'").getSingleResult())
            .getId());
    return query.getResultList();
  }

  @Override
  public void increaseBalance(Account account, BigDecimal amount) {
    Objects.requireNonNull(account);
    account = findOne(account.getAccountNumber()).get();
    account.setBalance(account.getBalance().add(amount));
    update(account);
  }

  @Override
  public void decreaseBalance(Account account, BigDecimal amount) {
    Objects.requireNonNull(account);
    account = findOne(account.getAccountNumber()).get();
    account.setBalance(account.getBalance().subtract(amount));
    update(account);
  }

  @Override
  public void updateAccountStatus(Account account, int statusId) {
    Objects.requireNonNull(account);
    account = findOne(account.getAccountNumber()).get();
    account.setStatus(new Status(statusId, "empty"));
    update(account);
  }


  /*
   * public static void main(String[] args) {
   * 
   * System.out.println("Find all:"); HibernateDebitAccountDao mySqlDebitAccountDao = new
   * HibernateDebitAccountDao(); ((HibernateDebitAccountDao)
   * mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());
   * 
   * int random = (int) (Math.random() * 100);
   * 
   * System.out.println("Find one:"); System.out.println(mySqlDebitAccountDao.findOne(3L));
   * 
   * System.out.println("find dy user:"); User user = User.newBuilder().addFirstName("first" +
   * random).addId(3).addLastName("last" + random) .addEmail("test" + random +
   * "@com").addPassword("123").addPhoneNumber("+123") .addRole(new
   * Role(Role.RoleIdentifier.USER_ROLE.getId(), "USER")).build();
   * System.out.println(mySqlDebitAccountDao.findByUser(user));
   * 
   * System.out.println("Insert:"); Account debitAccount = (Account) mySqlDebitAccountDao.insert(
   * Account.newBuilder().addAccountHolder(user).addAccountType(new AccountType(16, "DEBIT"))
   * .addBalance(BigDecimal.TEN).addStatus(new Status(1, "ACTIVE")).build());
   * 
   * System.out.println("Find all:"); ((HibernateDebitAccountDao)
   * mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());
   * 
   * System.out.println("update:"); debitAccount.setBalance(BigDecimal.valueOf(12345));
   * mySqlDebitAccountDao.update(debitAccount);
   * 
   * System.out.println("Find all:"); ((HibernateDebitAccountDao)
   * mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());
   * 
   * System.out.println("Increase:"); mySqlDebitAccountDao.increaseBalance(debitAccount,
   * BigDecimal.valueOf(100));
   * 
   * System.out.println("Find all:"); ((HibernateDebitAccountDao)
   * mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());
   * 
   * System.out.println("decrease:"); mySqlDebitAccountDao.decreaseBalance(debitAccount,
   * BigDecimal.valueOf(2000));
   * 
   * System.out.println("Find all:"); ((HibernateDebitAccountDao)
   * mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());
   * 
   * System.out.println("update status:"); mySqlDebitAccountDao.updateAccountStatus(debitAccount,
   * 4);
   * 
   * System.out.println("Find all:"); ((HibernateDebitAccountDao)
   * mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());
   * 
   * System.out.println("delete:"); mySqlDebitAccountDao.delete(debitAccount.getAccountNumber());
   * 
   * System.out.println("Find all:"); ((HibernateDebitAccountDao)
   * mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());
   * 
   * }
   * 
   * protected void printAccount(List<Account> list) { for (Account debitAccount : list) {
   * System.out.println("Account : " + debitAccount + ";"); System.out.println("Balance: " +
   * debitAccount.getBalance() + ";"); System.out.println(); } }
   */

}
