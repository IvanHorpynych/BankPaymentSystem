package dao.exception;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class DaoException extends RuntimeException{

    public DaoException(String message){
        super(message);
    }

    public DaoException(Throwable source) {
        super(source);
    }

    public DaoException(String message, Throwable source) {
        super(message,source);
    }

}
