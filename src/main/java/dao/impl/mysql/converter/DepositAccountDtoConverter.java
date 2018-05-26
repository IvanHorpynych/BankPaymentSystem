package dao.impl.mysql.converter;

import dao.util.time.TimeConverter;
import entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class DepositAccountDtoConverter implements DtoConverter<DepositAccount>{
    private final static String ACCOUNT_NUMBER_FIELD = "id";
    private final static String BALANCE_FIELD = "balance";
    private final static String MIN_BALANCE_FIELD = "min_balance";
    private final static String LAST_OPERATION_DATE_FIELD = "last_operation";
    private final static String ANNUAL_RATE_FIELD = "annual_rate";

    private final DtoConverter<User> userConverter;
    private final DtoConverter<AccountType> accountTypeConverter;
    private final DtoConverter<Status> statusConverter;
    private String accountOrder;

    public DepositAccountDtoConverter() {
        this(new UserDtoConverter(), new AccountTypeDtoConverter(),
                new StatusDtoConverter());
    }

    public DepositAccountDtoConverter(DtoConverter<User> userConverter,
                                      DtoConverter<AccountType> accountTypeConverter,
                                      DtoConverter<Status> statusConverter) {
        this.userConverter = userConverter;
        this.accountTypeConverter = accountTypeConverter;
        this.statusConverter = statusConverter;
    }

    @Override
    public DepositAccount convertToObject(ResultSet resultSet, String tablePrefix)
            throws SQLException {

        accountOrder = accountOrderIdentifier(tablePrefix);

        User accountHolder = userConverter.convertToObject(resultSet,
                accountOrder);
        AccountType accountType = accountTypeConverter.convertToObject(resultSet,
                accountOrder);
        Status status = statusConverter.convertToObject(resultSet,
                accountOrder);

        DepositAccount depositAccount = DepositAccount.newDepositBuilder().
                addAccountNumber(resultSet.
                        getLong(tablePrefix+ACCOUNT_NUMBER_FIELD)).
                addAccountHolder(accountHolder).
                addAccountType(accountType).
                addBalance(resultSet.getBigDecimal(tablePrefix+BALANCE_FIELD)).
                addMinBalance(resultSet.getBigDecimal(tablePrefix+MIN_BALANCE_FIELD)).
                addLastOperationDate(TimeConverter.
                        toDate(resultSet.getTimestamp(
                                tablePrefix+LAST_OPERATION_DATE_FIELD))).
                addAnnualRate(resultSet.getFloat(tablePrefix+ANNUAL_RATE_FIELD)).
                addStatus(status).
                build();

        return depositAccount;
    }
}
