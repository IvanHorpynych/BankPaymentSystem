package entity;


/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class RegularAccount extends Account{


    public RegularAccount() {
    }

    public static class Builder extends Account.AbstractBuilder<Builder>{
        private final RegularAccount account;

        public Builder() {
            account = new RegularAccount();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

    }

    public static RegularAccount.Builder newBuilder() {
        return new RegularAccount.Builder();
    }


}
