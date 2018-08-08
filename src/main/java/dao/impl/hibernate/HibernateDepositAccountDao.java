
package dao.impl.hibernate;

import dao.abstraction.DepositAccountDao;
import dao.config.HibernateUtil;
import entity.*;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HibernateDepositAccountDao implements DepositAccountDao {



  @Override
  public Optional<DepositAccount> findOne(Long accountNumber) {
    Session session = HibernateUtil.getCurrentSession();
    Query query = session.createQuery("from DepositAccount where accountNumber = :accountNumber",
        Account.class);
    query.setParameter("accountNumber", accountNumber);
    query.setMaxResults(1);
    try {
      return Optional.ofNullable((DepositAccount) query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }

  }

  @Override
  public List<DepositAccount> findAll() {
    Session session = HibernateUtil.getCurrentSession();
    Query query = session.createQuery("from DepositAccount ", DepositAccount.class);
    return query.getResultList();
  }

  @Override
  public DepositAccount insert(DepositAccount account) {
    Session session = HibernateUtil.getCurrentSession();
    Objects.requireNonNull(account);
    session.save(account);
    return account;
  }

  @Override
  public void update(DepositAccount account) {
    Session session = HibernateUtil.getCurrentSession();
    Objects.requireNonNull(account);
    session.update(account);
  }

  @Override
  public void delete(Long accountNumber) {
    Session session = HibernateUtil.getCurrentSession();
    Account account = findOne(accountNumber).get();
    session.delete(account);
  }

  @Override
  public List<DepositAccount> findByUser(User user) {
    Session session = HibernateUtil.getCurrentSession();
    Objects.requireNonNull(user);
    Query query = session.createQuery("from DepositAccount where accountHolder = :accountHolder",
        DepositAccount.class);
    query.setParameter("accountHolder", user);
    return query.getResultList();
  }

  @Override
  public List<DepositAccount> findAllNotClosed() {
    Session session = HibernateUtil.getCurrentSession();
    Query query =
        session.createQuery("from DepositAccount where status = :statusId", DepositAccount.class);
    query.setParameter("statusId",
        ((Status) session.createQuery("from Status where name != 'CLOSED'").getSingleResult())
            .getId());
    return query.getResultList();
  }

  @Override
  public void increaseBalance(DepositAccount account, BigDecimal amount) {
    Objects.requireNonNull(account);
    account = findOne(account.getAccountNumber()).get();
    account.setBalance(account.getBalance().add(amount));
    update(account);
  }

  @Override
  public void decreaseBalance(DepositAccount account, BigDecimal amount) {
    Objects.requireNonNull(account);
    account = findOne(account.getAccountNumber()).get();
    account.setBalance(account.getBalance().subtract(amount));
    update(account);
  }

  @Override
  public void updateAccountStatus(DepositAccount account, int statusId) {
    Objects.requireNonNull(account);

    account = findOne(account.getAccountNumber()).get();
    account.setStatus(new Status(statusId, "empty"));
    update(account);
  }


  @Override
  public void updateMinBalance(DepositAccount account, BigDecimal minBalance) {
    Objects.requireNonNull(account);
    account = findOne(account.getAccountNumber()).get();
    account.setMinBalance(minBalance);
    update(account);
  }

  /*
   * public static void main(String[] args) {
   * 
   * System.out.println("Find all:"); HibernateDepositAccountDao mySqlDepositAccountDao = new
   * HibernateDepositAccountDao(); ((HibernateDepositAccountDao) mySqlDepositAccountDao)
   * .printAccount(mySqlDepositAccountDao.findAll());
   * 
   * int random = (int) (Math.random() * 100);
   * 
   * System.out.println("Find one:"); System.out.println(mySqlDepositAccountDao.findOne(1L));
   * 
   * System.out.println("find dy user:"); User user = User.newBuilder().addFirstName("first" +
   * random).addId(2).addLastName("last" + random) .addEmail("test" + random +
   * "@com").addPassword("123").addPhoneNumber("+123") .addRole(new
   * Role(Role.RoleIdentifier.USER_ROLE.getId(), "USER")).build();
   * System.out.println(mySqlDepositAccountDao.findByUser(user));
   * 
   * System.out.println("Insert:"); DepositAccount depositAccount = (DepositAccount)
   * mySqlDepositAccountDao.insert(DepositAccount
   * .newDepositBuilder().addAccountHolder(user).addAccountType(new AccountType(8, "DEBIT"))
   * .addBalance(BigDecimal.TEN).addAnnualRate(2.5f).addLastOperationDate(new Date())
   * .addMinBalance(BigDecimal.ONE).addStatus(new Status(1, "ACTIVE")).build());
   * 
   * System.out.println("Find all:"); ((HibernateDepositAccountDao) mySqlDepositAccountDao)
   * .printAccount(mySqlDepositAccountDao.findAll());
   * 
   * System.out.println("update:"); depositAccount.setMinBalance(BigDecimal.ZERO);
   * mySqlDepositAccountDao.update(depositAccount);
   * 
   * System.out.println("Find all:"); ((HibernateDepositAccountDao) mySqlDepositAccountDao)
   * .printAccount(mySqlDepositAccountDao.findAll());
   * 
   * System.out.println("Increase:"); mySqlDepositAccountDao.increaseBalance(depositAccount,
   * BigDecimal.valueOf(100));
   * 
   * System.out.println("Find all:"); ((HibernateDepositAccountDao) mySqlDepositAccountDao)
   * .printAccount(mySqlDepositAccountDao.findAll());
   * 
   * System.out.println("decrease:"); mySqlDepositAccountDao.decreaseBalance(depositAccount,
   * BigDecimal.valueOf(2000));
   * 
   * System.out.println("Find all:"); ((HibernateDepositAccountDao) mySqlDepositAccountDao)
   * .printAccount(mySqlDepositAccountDao.findAll());
   * 
   * System.out.println("update status:");
   * mySqlDepositAccountDao.updateAccountStatus(depositAccount, 4);
   * 
   * System.out.println("Find all:"); ((HibernateDepositAccountDao) mySqlDepositAccountDao)
   * .printAccount(mySqlDepositAccountDao.findAll());
   * 
   * System.out.println("update min balance:");
   * mySqlDepositAccountDao.updateMinBalance(depositAccount, BigDecimal.TEN);
   * 
   * System.out.println("Find all:"); ((HibernateDepositAccountDao) mySqlDepositAccountDao)
   * .printAccount(mySqlDepositAccountDao.findAll());
   * 
   * System.out.println("delete:");
   * mySqlDepositAccountDao.delete(depositAccount.getAccountNumber());
   * 
   * System.out.println("Find all:"); ((HibernateDepositAccountDao) mySqlDepositAccountDao)
   * .printAccount(mySqlDepositAccountDao.findAll());
   * 
   * }
   * 
   * protected void printAccount(List<DepositAccount> list) { for (DepositAccount depositAccount :
   * list) { System.out.println("Account: " + depositAccount + ";"); System.out.println("Balance: "
   * + depositAccount.getBalance() + ";"); System.out.println("Annual Rate: " +
   * depositAccount.getAnnualRate() + ";"); System.out.println("Last operation: " +
   * depositAccount.getLastOperationDate() + ";"); System.out.println("Min Balance: " +
   * depositAccount.getMinBalance() + ";"); System.out.println(); } }
   */
}
