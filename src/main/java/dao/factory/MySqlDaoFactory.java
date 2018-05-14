package dao.factory;

import dao.abstraction.*;
import dao.connectionsource.PooledConnection;
import dao.exception.DaoException;
import dao.factory.connection.DaoConnection;
import dao.factory.connection.MySqlConnection;
import dao.impl.mysql.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlDaoFactory extends DaoFactory {
    private final static String NULLABLE_CONNECTION =
            "Null pointer connection!";

    private final static String WRONG_TYPE_CONNECTION =
            "Wrong type connection!";

    private DataSource dataSource = PooledConnection.getInstance();

    public DaoConnection getConnection() {
        try{
        return new MySqlConnection(dataSource.getConnection());
        } catch(SQLException e) {
           throw new DaoException(e);
        }
    }

    @Override
    public RoleDao getRoleDao(DaoConnection connection) {
        return new MySqlRoleDao(getOwnSqlConnection(connection));
    }

    public UserDao getUserDao(DaoConnection connection) {
        return new MySqlUserDao(getOwnSqlConnection(connection));
    }

    @Override
    public CreditAccountDao getCreditAccountDao(DaoConnection connection) {
        return new MySqlCreditAccountDao(getOwnSqlConnection(connection));
    }

    @Override
    public DebitAccountDao getDebitAccountDao(DaoConnection connection) {
        return new MySqlDebitAccountDao(getOwnSqlConnection(connection));
    }

    @Override
    public RegularAccountDao getRegularAccountDao(DaoConnection connection) {
        return new MySqlRegularAccountDao(getOwnSqlConnection(connection));
    }

    @Override
    public AccountTypeDao getAccountTypeDao(DaoConnection connection) {
        return new MySqlAccountTypeDao(getOwnSqlConnection(connection));
    }

    @Override
    public StatusDao getStatusDao(DaoConnection connection) {
        return new MySqlStatusDao(getOwnSqlConnection(connection));
    }

    @Override
    public CardDao getCardDao(DaoConnection connection) {
        return new MySqlCardDao(getOwnSqlConnection(connection));
    }

    @Override
    public PaymentDao getPaymentDao(DaoConnection connection) {
        return new MySqlPaymentDao(getOwnSqlConnection(connection));
    }

    @Override
    public CreditRequestDao getCreditRequestDao(DaoConnection connection) {
        return new MySqlCreditRequestDao(getOwnSqlConnection(connection));
    }


    private Connection getOwnSqlConnection(DaoConnection connection) {
        checkDaoConnection(connection);
        return (Connection) connection.getNativeConnection();
    }

    private void checkDaoConnection(DaoConnection connection) {
        if(connection == null || connection.getNativeConnection() == null) {
            throw new DaoException(NULLABLE_CONNECTION);
        }
        if(! (connection instanceof MySqlConnection)) {
            throw new DaoException(WRONG_TYPE_CONNECTION);
        }
    }
}