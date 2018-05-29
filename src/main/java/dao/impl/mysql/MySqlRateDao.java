
package dao.impl.mysql;

import dao.abstraction.RateDao;
import dao.abstraction.StatusDao;
import dao.datasource.PooledConnection;
import dao.impl.mysql.converter.DtoConverter;
import dao.impl.mysql.converter.RateDtoConverter;
import dao.impl.mysql.converter.StatusDtoConverter;
import entity.Rate;
import entity.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Created by JohnUkraine on 5/07/2018.
 */

public class MySqlRateDao implements RateDao {
    private final static String SELECT =
            "SELECT ANNUAL_RATE, CREATED_TIME FROM CURR_ANNUAL_RATE ";

    private final static String INSERT =
            "INSERT INTO CURR_ANNUAL_RATE (ANNUAL_RATE) " +
                    "VALUES(?);";

    private final static String UPDATE =
            "UPDATE status SET name = ? ";


    private final static String WHERE_ID =
            "WHERE id = ? ";

    private final static String LAST_INSERTED =
            "ORDER BY CREATED_TIME DESC ";


    private final DefaultDaoImpl<Rate> defaultDao;

    public MySqlRateDao(Connection connection) {
        this(connection, new RateDtoConverter());
    }

    public MySqlRateDao(Connection connection,
                        DtoConverter<Rate> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlRateDao(DefaultDaoImpl<Rate> defaultDao) {
        this.defaultDao = defaultDao;
    }

    @Override
    public Optional<Rate> findOne(Long id) {
        return defaultDao.findOne(SELECT + WHERE_ID, id);
    }

    @Override
    public List<Rate> findAll() {
        return defaultDao.findAll(SELECT);
    }

    @Override
    public Rate insert(Rate obj) {
        Objects.requireNonNull(obj, "Rate object must be not null");

        int id = (int) defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                obj.getAnnualRate()
        );

        obj.setId(id);

        return obj;
    }

    @Override
    public void update(Rate obj) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public Optional<Rate> findLast() {
        return defaultDao.findOne(SELECT+LAST_INSERTED);
    }



    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        RateDao mySqlRateDao;

        try {
            mySqlRateDao = new MySqlRateDao(dataSource.getConnection());
            ((MySqlRateDao) mySqlRateDao).printAll(mySqlRateDao.findAll());
            System.out.println();

            System.out.println("Find one with id 1:");
            System.out.println(mySqlRateDao.findOne(1L));


            System.out.println("Insert test:");
            Rate accountType = mySqlRateDao.
                    insert(new Rate(13.4f, new Date()));
            ((MySqlRateDao) mySqlRateDao).
                    printAll(mySqlRateDao.findAll());


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void printAll(List<Rate> list) {
        System.out.println("Find all:");
        for (Rate type : list) {
            System.out.println(type);
        }
    }




}

