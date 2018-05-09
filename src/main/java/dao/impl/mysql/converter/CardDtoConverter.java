package dao.impl.mysql.converter;

import dao.util.time.TimeConverter;
import entity.Account;
import entity.AccountType;
import entity.Card;
import entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class CardDtoConverter implements DtoConverter<Card> {

    private DtoConverter<? extends Account> accountConverter;
    /*private final DtoConverter<User> userConverter;

    public CardDtoConverter() {
        this(new UserDtoConverter());
    }

    public CardDtoConverter(DtoConverter<User> userConverter) {
        this.userConverter = userConverter;
    }*/

    @Override
    public Card convertToObject(ResultSet resultSet, String tablePrefix)
            throws SQLException {

        accountConverter = accountDtoSelection(
                resultSet.getInt(
                        AccountTypeDtoConverter.NAME_FIELD));
        Objects.requireNonNull(accountConverter, "AccountConverter object must be not null");
        Account cardAccount = accountConverter.
                convertToObject(resultSet);

       /* User cardHolder = userConverter.
                convertToObject(resultSet);*/

        Card card = Card.newBuilder().
                setCardNumber(resultSet.getLong(
                        tablePrefix + "card_number")).
                setAccount(cardAccount).
                setPin(resultSet.getInt(
                        tablePrefix + "pin")).
                setCvv(resultSet.getInt(
                        tablePrefix + "cvv")).
                setExpireDate(TimeConverter.toDate(
                        resultSet.getTimestamp(
                                tablePrefix + "expire_date"))).
                setType(Card.CardType.valueOf(
                        resultSet.getString(
                                tablePrefix + "type"))).
                build();

        return card;
    }
}
