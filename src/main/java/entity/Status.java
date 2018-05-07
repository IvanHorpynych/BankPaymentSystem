package entity;


/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class Status extends Designation {

    public final static int ACTIVE_STATUS_ID = 1;
    public final static int PENDING_STATUS_ID = 4;
    public final static int REJECT_STATUS_ID = 8;
    public final static int BLOCKED_STATUS_ID = 16;


    public Status() {};

    public Status(int id, String name) {
       super(id,name);
    }
}
