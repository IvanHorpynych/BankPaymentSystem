package dao.impl.mysql.converter;

import dao.util.time.TimeConverter;
import entity.Account;
import entity.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class PaymentDtoConverter implements DtoConverter<Payment> {
    private final static String ID_FIELD = "id";
    private final static String AMOUNT_FIELD = "amount";
    private final static String OPERATION_DATE_FIELD = "operation_date";

    private DtoConverter<? extends Account> accountConverter;
    private String accountTablePrefix;
    private int typeId;

    @Override
    public Payment convertToObject(ResultSet resultSet, String tablePrefix)
            throws SQLException {

        typeId = resultSet.getInt(FIRST_ACCOUNT_ORDER_TABLE_PREFIX +
                AccountTypeDtoConverter.ID_FIELD);
        accountTablePrefix = accountTablePrefixSelection(typeId);

        accountConverter = accountDtoSelection(typeId);
        Objects.requireNonNull(accountConverter,
                "AccountConverter object must be not null");

        Account accountFrom = accountConverter.
                convertToObject(resultSet, FIRST_ACCOUNT_ORDER_TABLE_PREFIX +
                        accountTablePrefix);


        typeId = resultSet.getInt(SECOND_ACCOUNT_ORDER_TABLE_PREFIX +
                AccountTypeDtoConverter.ID_FIELD);
        accountTablePrefix = accountTablePrefixSelection(typeId);

        accountConverter = accountDtoSelection(typeId);
        Objects.requireNonNull(accountConverter,
                "AccountConverter object must be not null");

        Account accountTo = accountConverter.
                convertToObject(resultSet, SECOND_ACCOUNT_ORDER_TABLE_PREFIX +
                        accountTablePrefix);


        Payment payment = Payment.newBuilder().
                setId(resultSet.getLong(
                        tablePrefix + ID_FIELD)).
                setAmount(resultSet.getBigDecimal(
                        tablePrefix + AMOUNT_FIELD)).
                setAccountFrom(accountFrom).
                setAccountTo(accountTo).
                setDate(TimeConverter.toDate(
                        resultSet.getTimestamp(
                                tablePrefix + OPERATION_DATE_FIELD))).
                build();

        return payment;
    }
}
