package dao.impl.mysql.converter;


import entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class AccountDtoConverter implements DtoConverter<Account>{
    private final static String ACCOUNT_NUMBER_FIELD = "id";
    private final static String BALANCE_FIELD = "balance";

    private final DtoConverter<User> userConverter;
    private final DtoConverter<AccountType> accountTypeConverter;
    private final DtoConverter<Status> statusConverter;
    private String accountOrder;
    public AccountDtoConverter() {
        this(new UserDtoConverter(), new AccountTypeDtoConverter(),
                new StatusDtoConverter());
    }

    public AccountDtoConverter(DtoConverter<User> userConverter,
                               DtoConverter<AccountType> accountTypeConverter,
                               DtoConverter<Status> statusConverter) {
        this.userConverter = userConverter;
        this.accountTypeConverter = accountTypeConverter;
        this.statusConverter = statusConverter;
    }

    @Override
    public Account convertToObject(ResultSet resultSet, String tablePrefix)
            throws SQLException {

        accountOrder = accountOrderIdentifier(tablePrefix);

        User accountHolder = userConverter.convertToObject(resultSet,
                accountOrder);
        AccountType accountType = accountTypeConverter.convertToObject(resultSet,
                accountOrder);
        Status status = statusConverter.convertToObject(resultSet,
                accountOrder);

        return Account.newBuilder().
                addAccountNumber(resultSet.
                        getLong(tablePrefix+ACCOUNT_NUMBER_FIELD)).
                addAccountHolder(accountHolder).
                addAccountType(accountType).
                addBalance(resultSet.getBigDecimal(tablePrefix+BALANCE_FIELD)).
                addStatus(status).
                build();
    }
}
