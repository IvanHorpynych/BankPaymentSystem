package entity;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class AccountType extends Designation {

    public final static int CREDIT_TYPE_ID = 4;
    public final static int DEPOSIT_TYPE_ID = 8;
    public final static int DEBIT_TYPE_ID = 16;

    public AccountType() {};

    public AccountType(int id, String name) {
        super(id, name);
    }
}
