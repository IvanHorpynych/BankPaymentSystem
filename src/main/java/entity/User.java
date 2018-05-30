package entity;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class User {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private Role role;

    private final static String DEFAULT_ROLE_NAME = "USER";

    private final static int DEFAULT_ROLE_ID = Role.RoleIdentifier.
            USER_ROLE.getId();

    public static class Builder{
        private final User user;

        public Builder() {
            user = new User();
        }

        public Builder addId(long id) {
            user.setId(id);
            return this;
        }

        public Builder addFirstName(String firstName) {
            user.setFirstName(firstName);
            return this;
        }

        public Builder addLastName(String lastName) {
            user.setLastName(lastName);
            return this;
        }

        public Builder addEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public Builder addPhoneNumber(String phoneNumber) {
            user.setPhoneNumber(phoneNumber);
            return this;
        }

        public Builder addPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public Builder addRole(Role role) {
            user.setRole(role);
            return this;
        }

        public User build() {
            return user;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isManager() {
        return role.getId() == Role.RoleIdentifier.MANAGER_ROLE.getId();
    }

    public boolean isUser() {
        return role.getId() == Role.RoleIdentifier.USER_ROLE.getId();
    }

    public void setDefaultRole() {
       this.role = new Role(DEFAULT_ROLE_ID,
               DEFAULT_ROLE_NAME);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email=" + email  +
                ", phoneNumber=" + phoneNumber  +
                ", password=" + password  +
                ", role=" + role  +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return (email.equals(user.email));
    }

}