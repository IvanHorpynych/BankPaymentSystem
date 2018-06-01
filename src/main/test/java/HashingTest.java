import dao.util.hashing.PasswordStorage;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by JohnUkraine on 06/01/2018.
 */
public class HashingTest {

    @Test
    public void checkingHash() {
        String password = "testPassword";

        String passwordHash = PasswordStorage.getSecurePassword(password);

        Assert.assertTrue(PasswordStorage.checkSecurePassword(password, passwordHash));
    }
}
