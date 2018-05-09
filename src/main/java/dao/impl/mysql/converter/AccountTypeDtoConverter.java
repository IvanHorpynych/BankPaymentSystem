package dao.impl.mysql.converter;

import entity.AccountType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class AccountTypeDtoConverter implements DtoConverter<AccountType> {
    protected final static String ID_FIELD = "type_id";
    protected final static String NAME_FIELD = "type_name";

    @Override
    public AccountType convertToObject(ResultSet resultSet, String tablePrefix) throws SQLException {
        int typeId = resultSet.getInt(tablePrefix + ID_FIELD);
        String typeName = resultSet.getString(tablePrefix + NAME_FIELD);
        return new AccountType(typeId,typeName);
    }
}
