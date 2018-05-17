package entity;


/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class DebitAccount extends Account{


    public DebitAccount() {
    }

    public static class Builder extends Account.AbstractBuilder<Builder, DebitAccount>{
        private final DebitAccount debitAccount;

        public Builder() {
            debitAccount = new DebitAccount();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        protected DebitAccount getAccount() {
            return debitAccount;
        }

        @Override
        public DebitAccount build() {
            return debitAccount;
        }

    }

    public static DebitAccount.Builder newBuilder() {
        return new DebitAccount.Builder();
    }


}
