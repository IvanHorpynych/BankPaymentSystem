import controller.util.validator.EmailValidator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by JohnUkraine on 06/01/2018.
 */
public class ValidatorTest {

    @Test
    public void validateEmail() {
        EmailValidator emailValidator = new EmailValidator();

        String rightTestEmail = "123@gmail.com";

        String wrongTestEmail = "&nwu%1!32cn;}|j";

        Assert.assertTrue(emailValidator.isValid(rightTestEmail));
        Assert.assertFalse(emailValidator.isValid(wrongTestEmail));
    }
}
