package dao.impl.mysql.converter;

import entity.Account;
import entity.AccountType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Common interface for all dto converters.
 *
 * @param <T> type of entity object
 *
 * @author JohnUkraine
 */
public interface DtoConverter <T> {
    String EMPTY_STRING = "";
    String CREDIT_TABLE_PREFIX = "credit_";
    String DEPOSIT_TABLE_PREFIX = "deposit_";
    String DEBIT_TABLE_PREFIX = "debit_";
    String FIRST_ACCOUNT_ORDER_TABLE_PREFIX = "acc1_";
    String SECOND_ACCOUNT_ORDER_TABLE_PREFIX = "acc2_";
    /**
     * Read data from a result set and convert it to list of objects.
     *
     * @param resultSet result set from the database
     * @return list of converted objects
     */
    default List<T> convertToObjectList(ResultSet resultSet)
            throws SQLException {
        return convertToObjectList(resultSet, EMPTY_STRING);
    }

    /**
     * Read data from a result set and convert it to list of objects.
     *
     * @param resultSet result set from the database
     * @param tablePrefix prefix of the table in result set
     * @return list of converted objects
     * @throws SQLException
     */
    default List<T> convertToObjectList(ResultSet resultSet, String tablePrefix)
            throws SQLException {
        List<T> convertedObjects = new ArrayList<T>();

        while (resultSet.next()){
            convertedObjects.add(convertToObject(resultSet, tablePrefix));
        }

        return convertedObjects;
    }

    /**
     * Read data from a result set and convert it to certain object.
     *
     * @param resultSet result set from the database
     * @return converted object
     * @throws SQLException
     */
    default T convertToObject(ResultSet resultSet) throws SQLException {
        return convertToObject(resultSet, EMPTY_STRING);
    }

    /**
     * Read data from a result set and convert it to certain object.
     *
     * @param resultSet result set from the database
     * @param tablePrefix prefix of the table in result set
     * @return converted object
     * @throws SQLException
     */
    T convertToObject(ResultSet resultSet, String tablePrefix) throws SQLException;

    default DtoConverter<? extends Account> accountDtoSelection(int typeId) {
        if (typeId == AccountType.CREDIT_TYPE_ID)
            return new CreditAccountDtoConverter();
        else if (typeId == AccountType.DEPOSIT_TYPE_ID)
            return new DepositAccountDtoConverter();
        else if (typeId == AccountType.DEBIT_TYPE_ID)
            return new DebitAccountDtoConverter();
        return null;
    }

    default String accountTablePrefixSelection(int typeId){
        if (typeId == AccountType.CREDIT_TYPE_ID)
            return CREDIT_TABLE_PREFIX;
        else if (typeId == AccountType.DEPOSIT_TYPE_ID)
            return DEPOSIT_TABLE_PREFIX;
        else if (typeId == AccountType.DEBIT_TYPE_ID)
            return DEBIT_TABLE_PREFIX;
        return null;
    }

    default String accountOrderIdentifier(String tablePrefix){
        if(tablePrefix.contains(FIRST_ACCOUNT_ORDER_TABLE_PREFIX))
            return FIRST_ACCOUNT_ORDER_TABLE_PREFIX;
        else if(tablePrefix.contains(SECOND_ACCOUNT_ORDER_TABLE_PREFIX))
            return SECOND_ACCOUNT_ORDER_TABLE_PREFIX;
        else return EMPTY_STRING;
    }

}
