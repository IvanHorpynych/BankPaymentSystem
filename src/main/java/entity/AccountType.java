package entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by JohnUkraine on 5/06/2018.
 */
@Entity
@Table(name = "ACCOUNT_TYPE")
public class AccountType extends Designation {

    public enum TypeIdentifier{

        CREDIT_TYPE (4), DEPOSIT_TYPE (8),
        DEBIT_TYPE(16), ATM_TYPE(32);

        private final int id;
        private TypeIdentifier(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public AccountType() {};

    public AccountType(int id, String name) {
        super(id, name);
    }
}
