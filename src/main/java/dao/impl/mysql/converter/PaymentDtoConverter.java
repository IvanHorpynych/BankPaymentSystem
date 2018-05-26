package dao.impl.mysql.converter;

import dao.util.time.TimeConverter;
import entity.Account;
import entity.AccountType;
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
    private final static String CARD_NUMBER_FROM = "card_number_from";

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
                addId(resultSet.getLong(
                        tablePrefix + ID_FIELD)).
                addAmount(resultSet.getBigDecimal(
                        tablePrefix + AMOUNT_FIELD)).
                addAccountFrom(accountFrom).
                addAccountTo(accountTo).
                addDate(TimeConverter.toDate(
                        resultSet.getTimestamp(
                                tablePrefix + OPERATION_DATE_FIELD))).
                addCardNumberFrom(resultSet.getLong(
                        tablePrefix + CARD_NUMBER_FROM)).
                build();

        return payment;
    }

    DtoConverter<? extends Account> accountDtoSelection(int typeId) {
        if (typeId == AccountType.TypeIdentifier.
                CREDIT_TYPE.getId())
            return new CreditAccountDtoConverter();
        else if (typeId == AccountType.TypeIdentifier.
                DEPOSIT_TYPE.getId())
            return new DepositAccountDtoConverter();
        else if (typeId == AccountType.TypeIdentifier.
                DEBIT_TYPE.getId())
            return new AccountDtoConverter();
        return null;
    }

    String accountTablePrefixSelection(int typeId){
        if (typeId == AccountType.TypeIdentifier.
                CREDIT_TYPE.getId())
            return CREDIT_TABLE_PREFIX;
        else if (typeId ==  AccountType.TypeIdentifier.
                DEPOSIT_TYPE.getId())
            return DEPOSIT_TABLE_PREFIX;
        else if (typeId == AccountType.TypeIdentifier.
                DEBIT_TYPE.getId())
            return DEBIT_TABLE_PREFIX;
        return null;
    }

}
