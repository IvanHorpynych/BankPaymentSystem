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

    private final static String ACCOUNT_FROM_INDEX = "acc1.";

    private final static String ACCOUNT_TO_INDEX = "acc2.";

    private final static String TYPE_FROM_INDEX = "type1.";

    private final static String TYPE_TO_INDEX = "type2.";


    private DtoConverter<? extends Account> accountConverter;

    @Override
    public Payment convertToObject(ResultSet resultSet, String tablePrefix) throws SQLException {
        accountConverter = accountDtoSelection(
                resultSet.getInt(TYPE_FROM_INDEX +
                        AccountTypeDtoConverter.NAME_FIELD));
        Objects.requireNonNull(accountConverter,
                "AccountConverter object must be not null");
        Account accountFrom = accountConverter.
                convertToObject(resultSet,
                        ACCOUNT_FROM_INDEX);


        accountConverter = accountDtoSelection(
                resultSet.getInt(TYPE_TO_INDEX +
                        AccountTypeDtoConverter.NAME_FIELD));
        Objects.requireNonNull(accountConverter,
                "AccountConverter object must be not null");
        Account accountTo = accountConverter.
                convertToObject(resultSet,
                        ACCOUNT_TO_INDEX);



        Payment payment = Payment.newBuilder().
                setId(resultSet.getInt(
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
