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
public class CreditAccountDtoConverter implements DtoConverter<CreditAccount> {

    private final static String ACCOUNT_NUMBER_FIELD = "id";
    private final static String BALANCE_FIELD = "balance";
    private final static String CREDIT_LIMIT_FIELD = "credit_limit";
    private final static String INTEREST_RATE_FIELD = "interest_rate";
    private final static String LAST_OPERATION_DATE_FIELD = "last_operation";
    private final static String ACCRUED_INTEREST_FIELD = "accrued_interest";
    private final static String VALIDITY_DATE_FIELD = "validity_date";

    private final DtoConverter<User> userConverter;
    private final DtoConverter<AccountType> accountTypeConverter;
    private final DtoConverter<Status> statusConverter;
    private String accountOrder;

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

        accountOrder = accountOrderIdentifier(tablePrefix);

        User accountHolder = userConverter.convertToObject(resultSet,
                accountOrder);
        AccountType accountType = accountTypeConverter.convertToObject(resultSet,
                accountOrder);
        Status status = statusConverter.convertToObject(resultSet,
                accountOrder);

        return CreditAccount.newCreditBuilder().
                addAccountNumber(resultSet.
                        getLong(tablePrefix + ACCOUNT_NUMBER_FIELD)).
                addAccountHolder(accountHolder).
                addAccountType(accountType).
                addBalance(resultSet.getBigDecimal(tablePrefix + BALANCE_FIELD)).
                addCreditLimit(resultSet.
                        getBigDecimal(tablePrefix + CREDIT_LIMIT_FIELD)).
                addInterestRate(resultSet.
                        getFloat(tablePrefix + INTEREST_RATE_FIELD)).
                addAccruedInterest(resultSet.
                        getBigDecimal(tablePrefix + ACCRUED_INTEREST_FIELD)).
                addValidityDate(TimeConverter.
                        toDate(resultSet.getTimestamp(
                                tablePrefix + VALIDITY_DATE_FIELD))).
                addStatus(status).
                build();

    }
}
