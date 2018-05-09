package dao.impl.mysql.converter;


import entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class RegularAccountDtoConverter implements DtoConverter<Account>{
    private final static String ACCOUNT_NUMBER_FIELD = "id";
    private final static String BALANCE_FIELD = "balance";

    private final DtoConverter<User> userConverter;
    private final DtoConverter<AccountType> accountTypeConverter;
    private final DtoConverter<Status> statusConverter;

    public RegularAccountDtoConverter() {
        this(new UserDtoConverter(), new AccountTypeDtoConverter(),
                new StatusDtoConverter());
    }

    public RegularAccountDtoConverter(DtoConverter<User> userConverter,
                                    DtoConverter<AccountType> accountTypeConverter,
                                    DtoConverter<Status> statusConverter) {
        this.userConverter = userConverter;
        this.accountTypeConverter = accountTypeConverter;
        this.statusConverter = statusConverter;
    }

    @Override
    public RegularAccount convertToObject(ResultSet resultSet, String tablePrefix)
            throws SQLException {

        User accountHolder = userConverter.convertToObject(resultSet);
        AccountType accountType = accountTypeConverter.convertToObject(resultSet);
        Status status = statusConverter.convertToObject(resultSet);

        RegularAccount regularAccount = RegularAccount.newBuilder().
                setAccountNumber(resultSet.
                        getLong(tablePrefix+ACCOUNT_NUMBER_FIELD)).
                setAccountHolder(accountHolder).
                setAccountType(accountType).
                setBalance(resultSet.getBigDecimal(tablePrefix+BALANCE_FIELD)).
                setStatus(status).
                build();

        return regularAccount;
    }
}
