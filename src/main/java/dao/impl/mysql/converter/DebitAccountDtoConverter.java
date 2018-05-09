package dao.impl.mysql.converter;

import dao.util.time.TimeConverter;
import entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class DebitAccountDtoConverter implements DtoConverter<DebitAccount>{
    private final static String ACCOUNT_NUMBER_FIELD = "id";
    private final static String BALANCE_FIELD = "balance";
    private final static String MIN_BALANCE_FIELD = "min_balance";
    private final static String LAST_OPERATION_DATE_FIELD = "last_operation";
    private final static String ANNUAL_RATE_FIELD = "annual_rate";

    private final DtoConverter<User> userConverter;
    private final DtoConverter<AccountType> accountTypeConverter;
    private final DtoConverter<Status> statusConverter;

    public DebitAccountDtoConverter() {
        this(new UserDtoConverter(), new AccountTypeDtoConverter(),
                new StatusDtoConverter());
    }

    public DebitAccountDtoConverter(DtoConverter<User> userConverter,
                                     DtoConverter<AccountType> accountTypeConverter,
                                     DtoConverter<Status> statusConverter) {
        this.userConverter = userConverter;
        this.accountTypeConverter = accountTypeConverter;
        this.statusConverter = statusConverter;
    }

    @Override
    public DebitAccount convertToObject(ResultSet resultSet, String tablePrefix)
            throws SQLException {

        User accountHolder = userConverter.convertToObject(resultSet);
        AccountType accountType = accountTypeConverter.convertToObject(resultSet);
        Status status = statusConverter.convertToObject(resultSet);

        DebitAccount debitAccount = DebitAccount.newBuilder().
                setAccountNumber(resultSet.
                        getLong(tablePrefix+ACCOUNT_NUMBER_FIELD)).
                setAccountHolder(accountHolder).
                setAccountType(accountType).
                setBalance(resultSet.getBigDecimal(tablePrefix+BALANCE_FIELD)).
                setMinBalance(resultSet.getBigDecimal(tablePrefix+MIN_BALANCE_FIELD)).
                setLastOperationDate(TimeConverter.
                        toDate(resultSet.getTimestamp(
                                tablePrefix+LAST_OPERATION_DATE_FIELD))).
                setAnnualRate(resultSet.getFloat(tablePrefix+ANNUAL_RATE_FIELD)).
                setStatus(status).
                build();

        return debitAccount;
    }
}
