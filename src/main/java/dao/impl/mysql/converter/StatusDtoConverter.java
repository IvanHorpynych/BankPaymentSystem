package dao.impl.mysql.converter;

import entity.Status;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class StatusDtoConverter implements DtoConverter<Status> {
    private final static String ID_FIELD = "status_id";
    private final static String NAME_FIELD = "status_name";

    @Override
    public Status convertToObject(ResultSet resultSet, String tablePrefix) throws SQLException {
        int statusId = resultSet.getInt(tablePrefix + ID_FIELD);
        String statusName = resultSet.getString(tablePrefix + NAME_FIELD);
        return new Status(statusId,statusName);
    }
}
