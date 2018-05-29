package dao.impl.mysql.converter;

import dao.util.time.TimeConverter;
import entity.Rate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class RateDtoConverter implements DtoConverter<Rate> {
    private final static String RATE_FIELD = "ANNUAL_RATE";
    private final static String TIME_FIELD = "CREATED_TIME";

    @Override
    public Rate convertToObject(ResultSet resultSet, String tablePrefix) throws SQLException {
        float rate = resultSet.getFloat(tablePrefix + RATE_FIELD);
        Date createdTime = TimeConverter.toDate(
                resultSet.getTimestamp(tablePrefix + TIME_FIELD));
        return new Rate(rate,createdTime);
    }
}
