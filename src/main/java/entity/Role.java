package entity;

/**
 * Created by JohnUkraine on 5/06/2018.
 */
public class Role extends Designation{

    public enum RoleIdentifier{

        MANAGER_ROLE (2), USER_ROLE (10);

        private final int id;
        private RoleIdentifier(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }


    public Role() {};

    public Role(int id, String name) {
       super(id, name);
    }

}
