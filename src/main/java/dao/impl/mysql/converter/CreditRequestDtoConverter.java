package dao.impl.mysql.converter;

import dao.util.time.TimeConverter;
import entity.Account;
import entity.CreditRequest;
import entity.Status;
import entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class CreditRequestDtoConverter implements DtoConverter<CreditRequest>{
    private final static String REQUEST_NUMBER_FIELD = "id";
    private final static String CREDIT_LIMIT_FIELD = "credit_limit";
    private final static String INTEREST_RATE_FIELD = "interest_rate";
    private final static String VALIDITY_DATE_FIELD = "validity_date";

    private final DtoConverter<User> userConverter;
    private final DtoConverter<Status> statusConverter;

    public CreditRequestDtoConverter() {
        this(new UserDtoConverter(), new StatusDtoConverter());
    }

    public CreditRequestDtoConverter(DtoConverter<User> userConverter, DtoConverter<Status> statusConverter) {
        this.userConverter = userConverter;
        this.statusConverter = statusConverter;
    }

    @Override
    public CreditRequest convertToObject(ResultSet resultSet, String tablePrefix)
            throws SQLException {

       User accountHolder = userConverter.convertToObject(resultSet);
       Status status = statusConverter.convertToObject(resultSet);

       CreditRequest creditRequest =CreditRequest.newBuilder().
               addRequestNumber(resultSet.
                       getLong(tablePrefix+REQUEST_NUMBER_FIELD)).
               addAccountHolder(accountHolder).
               addInterestRate(resultSet.
                       getFloat(tablePrefix+INTEREST_RATE_FIELD)).
               addStatus(status).
               addValidityDate(TimeConverter.
                       toDate(resultSet.getTimestamp(
                               tablePrefix+VALIDITY_DATE_FIELD))).
               addCreditLimit(resultSet.
                       getBigDecimal(tablePrefix+CREDIT_LIMIT_FIELD)).
               build();

        return creditRequest;
    }
}
