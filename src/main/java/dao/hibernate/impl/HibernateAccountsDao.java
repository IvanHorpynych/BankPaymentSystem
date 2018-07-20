
package dao.hibernate.impl;

import dao.abstraction.AccountsDao;
import dao.hibernate.HibernateUtil;
import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class HibernateAccountsDao implements AccountsDao {


    @Override
    public Optional<Account> findOne(Long accountNumber) {
        try (Session session = HibernateUtil.getInstance()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
            Root<Account> root = query.from(Account.class);
            query.select(root);
            query.where(criteriaBuilder.equal(root.get("accountNumber"), accountNumber));
            return Optional.ofNullable(session.createQuery(query).getSingleResult());
        }
    }

    @Override
    public List<Account> findAll() {
        try (Session session = HibernateUtil.getInstance()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
            Root<Account> root = query.from(Account.class);
            query.select(root);
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public Account insert(Account account) {
        try (Session session = HibernateUtil.getInstance()) {
            session.save(account);
            return account;
        }
    }

    @Override
    public void update(Account account) {
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
    public List<Account> findByUser(User user) {
        try (Session session = HibernateUtil.getInstance()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
            Root<Account> root = query.from(Account.class);
            query.select(root);
            query.where(criteriaBuilder.equal(root.get("accountHolder"), user.getId()));
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Account> findAllNotClosed() {
        try (Session session = HibernateUtil.getInstance()) {
            Query query = session.createQuery("from Account where status = :statusId", Account.class);
            query.setParameter("statusId", session.createQuery("from Status where name != 'CLOSED'").getParameterValue("id"));
           return query.getResultList();
        }
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

        Objects.requireNonNull(account);
        account = findOne(account.getAccountNumber()).get();
        account.setStatus(new Status(statusId,"empty"));
        update(account);
    }

    @Override
    public Optional<Account> findOneByType(int typeId) {
        try (Session session = HibernateUtil.getInstance()) {
            Query query = session.createQuery("from Account where accountType = :accountType", Account.class);
            query.setParameter("accountType",typeId);
            query.setMaxResults(1);
            return Optional.ofNullable((Account) query.getSingleResult());
        }
    }


    /*public static void main(String[] args) {
        AccountsDao mySqlDebitAccountDao;

            System.out.println("Find all:");
        mySqlDebitAccountDao = new HibernateAccountsDao();
            ((HibernateAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            int random = (int) (Math.random() * 100);

            System.out.println("Find one:");
            System.out.println(mySqlDebitAccountDao.findOne(2L));

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
            System.out.println(mySqlDebitAccountDao.findByUser(user));

            System.out.println("Insert:");
            Account debitAccount = (Account) mySqlDebitAccountDao.insert(
                    Account.newBuilder().
                            addAccountHolder(user).
                            addAccountType(new AccountType(16,"DEBIT")).
                            addBalance(BigDecimal.TEN).
                            addStatus(new Status(1,"ACTIVE")).
                            build()
            );

            System.out.println("Find all:");
            ((HibernateAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("update:");
            debitAccount.setBalance(BigDecimal.valueOf(12345));
            mySqlDebitAccountDao.update(debitAccount);

            System.out.println("Find all:");
            ((HibernateAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("Increase:");
            mySqlDebitAccountDao.increaseBalance(debitAccount, BigDecimal.valueOf(100));

            System.out.println("Find all:");
            ((HibernateAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("decrease:");
            mySqlDebitAccountDao.decreaseBalance(debitAccount, BigDecimal.valueOf(2000));

            System.out.println("Find all:");
            ((HibernateAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("update status:");
            mySqlDebitAccountDao.updateAccountStatus(debitAccount,4);

            System.out.println("Find all:");
            ((HibernateAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("delete:");
            mySqlDebitAccountDao.delete(debitAccount.getAccountNumber());

            System.out.println("Find all:");
            ((HibernateAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());


    }

    protected void printAccount(List<Account> list){
        for (Account debitAccount : list) {
            System.out.println("Account : "+debitAccount+";");
            System.out.println("Balance: "+debitAccount.getBalance()+";");
            System.out.println();
        }
    }*/

}
