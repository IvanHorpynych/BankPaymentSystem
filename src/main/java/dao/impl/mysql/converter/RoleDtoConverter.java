package dao.impl.mysql.converter;

import entity.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class RoleDtoConverter implements DtoConverter<Role> {
    private final static String ID_FIELD = "role_id";
    private final static String NAME_FIELD = "role_name";

    @Override
    public Role convertToObject(ResultSet resultSet, String tablePrefix) throws SQLException {
        int roleId = resultSet.getInt(tablePrefix + ID_FIELD);
        String roleName = resultSet.getString(tablePrefix + NAME_FIELD);
        return new Role(roleId,roleName);
    }
}
