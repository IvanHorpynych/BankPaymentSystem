package dao.impl.mysql.converter;

import dao.util.time.TimeConverter;
import entity.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class CreditAccountDtoConverter implements DtoConverter<CreditAccount>{

    private final static String ACCOUNT_NUMBER_FIELD = "id";
    private final static String BALANCE_FIELD = "balance";
    private final static String CREDIT_LIMIT_FIELD = "credit_limit";
    private final static String INTEREST_RATE_FIELD = "interest_rate";
    private final static String LAST_OPERATION_DATE_FIELD = "last_operation";
    private final static String ACCRUED_INTEREST_FIELD = "accrued_interest";
    private final static String VALIDITY_DATE_FIELD = "validity_date";
    private final static String STATUS_TABLE_PREFIX = "status.";
    private final static String USER_TABLE_PREFIX = "user.";

    private final DtoConverter<User> userConverter;
    private final DtoConverter<AccountType> accountTypeConverter;
    private final DtoConverter<Status> statusConverter;

    public CreditAccountDtoConverter() {
        this(new UserDtoConverter(), new AccountTypeDtoConverter(),
                new StatusDtoConverter());
    }

    private CreditAccountDtoConverter(DtoConverter<User> userConverter,
                                     DtoConverter<AccountType> accountTypeConverter,
                                     DtoConverter<Status> statusConverter) {
        this.userConverter = userConverter;
        this.accountTypeConverter = accountTypeConverter;
        this.statusConverter = statusConverter;
    }

    @Override
    public CreditAccount convertToObject(ResultSet resultSet, String tablePrefix)
            throws SQLException {

       User accountHolder = userConverter.convertToObject(resultSet);
       AccountType accountType = accountTypeConverter.convertToObject(resultSet);
       Status status = statusConverter.convertToObject(resultSet,STATUS_TABLE_PREFIX);

        return  CreditAccount.newBuilder().
               setAccountNumber(resultSet.
                       getLong(tablePrefix+ACCOUNT_NUMBER_FIELD)).
               setAccountHolder(accountHolder).
               setAccountType(accountType).
               setBalance(resultSet.getBigDecimal(tablePrefix+BALANCE_FIELD)).
               setCreditLimit(resultSet.
                       getBigDecimal(tablePrefix+CREDIT_LIMIT_FIELD)).
               setInterestRate(resultSet.
                       getLong(tablePrefix+INTEREST_RATE_FIELD)).
               setLastOperationDate(TimeConverter.
                       toDate(resultSet.getTimestamp(
                               tablePrefix+VALIDITY_DATE_FIELD))).
               setAccruedInterest(resultSet.
                       getBigDecimal(tablePrefix+ACCRUED_INTEREST_FIELD)).
               setValidityDate(TimeConverter.
                       toDate(resultSet.getTimestamp(
                               tablePrefix+LAST_OPERATION_DATE_FIELD))).
               setStatus(status).
               build();

    }
}
