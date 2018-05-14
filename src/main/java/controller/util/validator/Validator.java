package controller.util.validator;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public interface Validator<T> {
    /**
     * Method that get error status of validation process
     *
     * @return error message in case if input isn't valid,
     *         otherwise return {@code null}
     */
    String getErrorKey();

    /**
     * Check is input message is valid
     *
     * @param obj that need to check
     * @return {@code true} if input is valid
     *         {@code false} if input is not valid
     */
    boolean isValid(T obj);

    /**
     * Set error state to initial value.
     */
    void resetErrorStatus();
}
