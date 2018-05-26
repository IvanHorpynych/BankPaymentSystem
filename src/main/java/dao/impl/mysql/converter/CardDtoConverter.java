package dao.impl.mysql.converter;

import dao.util.time.TimeConverter;
import entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class CardDtoConverter implements DtoConverter<Card> {

    private String CARD_TABLE_PREFIX = "card_";
    private DtoConverter<Account> accountConverter;
    private DtoConverter<Status> statusConverter;

    public CardDtoConverter() {
        this( new AccountDtoConverter(), new StatusDtoConverter());
    }

    public CardDtoConverter(DtoConverter<Account> accountConverter, DtoConverter<Status> statusDtoConverter) {
        this.accountConverter = accountConverter;
        this.statusConverter = statusDtoConverter;
    }

    @Override
    public Card convertToObject(ResultSet resultSet, String tablePrefix)
            throws SQLException {

        Objects.requireNonNull(accountConverter, "AccountConverter object must be not null");
        Account cardAccount = accountConverter.
                convertToObject(resultSet,DEBIT_TABLE_PREFIX);

        Status cardStatus = statusConverter.convertToObject(resultSet,CARD_TABLE_PREFIX);


        Card card = Card.newBuilder().
                addCardNumber(resultSet.getLong(
                        tablePrefix + "card_number")).
                addAccount(cardAccount).
                addPin(resultSet.getInt(
                        tablePrefix + "pin")).
                addCvv(resultSet.getInt(
                        tablePrefix + "cvv")).
                addExpireDate(TimeConverter.toDate(
                        resultSet.getTimestamp(
                                tablePrefix + "expire_date"))).
                addType(Card.CardType.valueOf(
                        resultSet.getString(
                                tablePrefix + "type"))).
                addStatus(cardStatus).
                build();

        return card;
    }
}
