
package dao.hibernate.impl;

import dao.abstraction.AccountsDao;
import dao.datasource.PooledConnection;
import dao.hibernate.HibernateUtil;
import dao.impl.mysql.DefaultDaoImpl;
import dao.impl.mysql.converter.AccountDtoConverter;
import dao.impl.mysql.converter.DtoConverter;
import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class HibernateAccountsDao implements AccountsDao {


    /*private String SELECT_ALL;

    private final static String MAIN_QUERY =
            "SELECT * FROM account_details ";

    private final static String WHERE_ACCOUNT_NUMBER =
            "WHERE id = ? ";

    private final static String WHERE_USER =
            "WHERE user_id = ? ";

    private final static String WHERE_NOT_CLOSED =
            "WHERE status_id != " +
                    "(SELECT id FROM status where name = 'CLOSED') ";

    private final static String WHERE_TYPE =
            "WHERE type_id = ? ";

    private final static String INSERT =
            "INSERT INTO account " +
                    "(user_id, type_id, status_id, balance) " +
                    "VALUES(?, ?, ?, ?) ";


    private final static String UPDATE =
            "UPDATE account SET " +
                    "balance = ? ";

    private final static String UPDATE_STATUS =
            "UPDATE account SET " +
                    "status_id = ? ";

    private final static String INCREASE_BALANCE =
            "UPDATE account SET " +
                    "balance = balance + ? ";

    private final static String DECREASE_BALANCE =
            "UPDATE account SET " +
                    "balance = balance - ? ";

    private final static String DELETE =
            "DELETE FROM account ";*/


    @Override
    public Optional<Account> findOne(Long accountNumber) {
        try (Session session = HibernateUtil.getInstance()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
            Root<Account> root = query.from(Account.class);
            query.select(root);
            query.where(criteriaBuilder.equal(root.get("id"), accountNumber));
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
            query.where(criteriaBuilder.equal(root.get("user_id"), user.getId()));
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Account> findAllNotClosed() {
        try (Session session = HibernateUtil.getInstance()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaQuery<Status> secondQuery = criteriaBuilder.createQuery(Status.class);
            Root<Status> secondRoot = secondQuery.from(Status.class);

            CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
            Root<Account> root = query.from(Account.class);

            query.select(root);
            query.where(criteriaBuilder.equal(root.get("status"),secondRoot.get());
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public void increaseBalance(Account account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                INCREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void decreaseBalance(Account account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                DECREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void updateAccountStatus(Account account, int statusId) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE_STATUS + WHERE_ACCOUNT_NUMBER,
                statusId,
                account.getAccountNumber()
        );
    }

    @Override
    public Optional<Account> findOneByType(int typeId) {
        return defaultDao.findOne(SELECT_ALL+WHERE_TYPE, typeId);
    }


    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        AccountsDao mySqlDebitAccountDao;
        try {
            System.out.println("Find all:");
            mySqlDebitAccountDao = new HibernateAccountsDao(dataSource.getConnection());
            ((HibernateAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            int random = (int) (Math.random() * 100);

            System.out.println("Find one:");
            System.out.println(mySqlDebitAccountDao.findOne(3L));

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
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void printAccount(List<Account> list){
        for (Account debitAccount : list) {
            System.out.println("Account : "+debitAccount+";");
            System.out.println("Balance: "+debitAccount.getBalance()+";");
            System.out.println();
        }
    }

}
