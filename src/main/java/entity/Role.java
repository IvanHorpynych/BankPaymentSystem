package entity;

/**
 * Created by JohnUkraine on 5/06/2018.
 */
public class Role extends Designation{

    public final static int MANAGER_ROLE_ID = 1;
    public final static int USER_ROLE_ID = 10;

    public Role() {};

    public Role(int id, String name) {
       super(id, name);
    }

}
