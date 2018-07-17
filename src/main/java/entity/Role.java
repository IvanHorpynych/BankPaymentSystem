package entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by JohnUkraine on 5/06/2018.
 */
@Entity
public class Role extends Designation{

    @OneToMany(mappedBy = "role")
    private List<User> users;

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
