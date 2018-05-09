package entity;


/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class RegularAccount extends Account{


    public RegularAccount() {
    }

    public static class Builder extends Account.AbstractBuilder<Builder, RegularAccount>{
        private final RegularAccount regularAccount;

        public Builder() {
            regularAccount = new RegularAccount();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        protected RegularAccount getAccount() {
            return regularAccount;
        }

        @Override
        public RegularAccount build() {
            return regularAccount;
        }

    }

    public static RegularAccount.Builder newBuilder() {
        return new RegularAccount.Builder();
    }


}
