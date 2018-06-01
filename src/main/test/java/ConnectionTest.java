import entity.User;
import org.junit.Assert;
import org.junit.Test;
import service.ServiceFactory;
import service.UserService;

import java.util.List;

/**
 * Created by JohnUkraine on 06/01/2018.
 */
public class ConnectionTest {

    @Test
    public void checkConnection(){
        UserService userService = ServiceFactory.getUserService();

        List<User> users = userService.findAllUsers();

        Assert.assertFalse(users.isEmpty());
    }

}
