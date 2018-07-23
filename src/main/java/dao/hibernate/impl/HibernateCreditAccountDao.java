package dao.hibernate.impl;

import dao.abstraction.CreditAccountDao;
import dao.datasource.PooledConnection;
import dao.hibernate.HibernateUtil;
import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HibernateCreditAccountDao implements CreditAccountDao {


    @Override
    public Optional<CreditAccount> findOne(Long accountNumber) {
        try (Session session = HibernateUtil.getInstance()) {
            Query query = session.createQuery("from CreditAccount where accountNumber = :accountNumber", CreditAccount.class);
            query.setParameter("accountNumber", accountNumber);
            query.setMaxResults(1);
            try{
                return Optional.ofNullable((CreditAccount) query.getSingleResult());
            } catch (NoResultException e){
                return Optional.empty();
            }

        }
    }

    @Override
    public List<CreditAccount> findAll() {
        try (Session session = HibernateUtil.getInstance()) {
            Query query = session.createQuery("from CreditAccount", CreditAccount.class);
            return query.getResultList();
        }
    }

    @Override
    public CreditAccount insert(CreditAccount account) {
        Objects.requireNonNull(account);
        try (Session session = HibernateUtil.getInstance()) {
            session.save(account);
            return account;
        }
    }

    @Override
    public void update(CreditAccount account) {
        Objects.requireNonNull(account);
        try (Session session = HibernateUtil.getInstance()) {
            Transaction transaction = session.beginTransaction();
            session.update(account);
            transaction.commit();
        }
    }

    @Override
    public void delete(Long accountNumber) {
        try (Session session = HibernateUtil.getInstance()) {
            Transaction transaction = session.beginTransaction();
            Account account = findOne(accountNumber).get();
            session.delete(account);
            transaction.commit();
        }

    }

    @Override
    public List<CreditAccount> findByUser(User user) {
        Objects.requireNonNull(user);
        try (Session session = HibernateUtil.getInstance()) {
            Query query = session.createQuery("from CreditAccount where accountHolder = :accountHolder", CreditAccount.class);
            query.setParameter("accountHolder", user);
            query.setMaxResults(1);
            return  query.getResultList();
        }
    }

    @Override
    public List<CreditAccount> findAllNotClosed() {
        try (Session session = HibernateUtil.getInstance()) {
            Query query = session.createQuery("from CreditAccount where status = :statusId", CreditAccount.class);
            query.setParameter("statusId", session.createQuery("from Status where name != 'CLOSED'").getParameterValue("id"));
            return query.getResultList();
        }
    }

    @Override
    public void increaseBalance(CreditAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);
        account = findOne(account.getAccountNumber()).get();
        account.setBalance(account.getBalance().add(amount));
        update(account);
    }

    @Override
    public void decreaseBalance(CreditAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);
        account = findOne(account.getAccountNumber()).get();
        account.setBalance(account.getBalance().subtract(amount));
        update(account);
    }

    @Override
    public void updateAccountStatus(CreditAccount account, int statusId) {
        Objects.requireNonNull(account);

        Objects.requireNonNull(account);
        account = findOne(account.getAccountNumber()).get();
        account.setStatus(new Status(statusId,"empty"));
        update(account);
    }


    @Override
    public void increaseAccruedInterest(CreditAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        account = findOne(account.getAccountNumber()).get();
        account.setAccruedInterest(account.getAccruedInterest().add(amount));
        update(account);
    }

    @Override
    public void decreaseAccruedInterest(CreditAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        Objects.requireNonNull(account);

        account = findOne(account.getAccountNumber()).get();
        account.setAccruedInterest(account.getAccruedInterest().divide(amount));
        update(account);
    }


    public static void main(String[] args) {
        HibernateCreditAccountDao mySqlCreditAccountDao;
            System.out.println("Find all:");
            mySqlCreditAccountDao = new HibernateCreditAccountDao();
            ((HibernateCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            int random = (int) (Math.random() * 100);

            System.out.println("Find one:");
            System.out.println(mySqlCreditAccountDao.findOne(2L));

            System.out.println("find dy user:");
            User user = User.newBuilder().addFirstName("first" + random).
                    addId(3).
                    addLastName("last" + random).
                    addEmail("test" + random + "@com").
                    addPassword("123").
                    addPhoneNumber("+123").
                    addRole(new Role(Role.RoleIdentifier.
                            USER_ROLE.getId(), "USER")).
                    build();
            System.out.println(mySqlCreditAccountDao.findByUser(user));

            System.out.println("Insert:");
            CreditAccount creditAccount = (CreditAccount) mySqlCreditAccountDao.insert(
                    CreditAccount.newCreditBuilder().
                            addAccountHolder(user).
                            addAccountType(new AccountType(4,"CREDIT")).
                            addBalance(BigDecimal.ONE).
                            addCreditLimit(BigDecimal.TEN).
                            addInterestRate(2L).
                            addAccruedInterest(BigDecimal.ZERO).
                            addValidityDate(new Date()).
                            addStatus(new Status(1,"ACTIVE")).
                            build()
            );

            System.out.println("Find all:");
            ((HibernateCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            System.out.println("update:");
            creditAccount.setAccruedInterest(BigDecimal.valueOf(12345));
            mySqlCreditAccountDao.update(creditAccount);

            System.out.println("Find all:");
            ((HibernateCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            System.out.println("Increase:");
            mySqlCreditAccountDao.increaseBalance(creditAccount, BigDecimal.valueOf(100));

            System.out.println("Find all:");
            ((HibernateCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            System.out.println("decrease:");
            mySqlCreditAccountDao.decreaseBalance(creditAccount, BigDecimal.valueOf(100));

            System.out.println("Find all:");
            ((HibernateCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            System.out.println("update status:");
            mySqlCreditAccountDao.updateAccountStatus(creditAccount,4);

            System.out.println("Find all:");
            ((HibernateCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            System.out.println("delete:");
            mySqlCreditAccountDao.delete(creditAccount.getAccountNumber());

            System.out.println("Find all:");
            ((HibernateCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

    }

    protected void printAccount(List<CreditAccount> list){
        for (CreditAccount creditAccount : list) {
            System.out.println("Account: "+creditAccount+";");
            System.out.println("Balance: "+creditAccount.getBalance()+";");
            System.out.println("Credit limit: "+creditAccount.getCreditLimit()+";");
            System.out.println("Interest Rate: "+creditAccount.getInterestRate()+";");
            System.out.println("Accrued interest: "+creditAccount.getAccruedInterest()+";");
            System.out.println("Validity date: "+creditAccount.getValidityDate()+";");
            System.out.println();
        }
    }

}
