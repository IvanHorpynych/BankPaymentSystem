package entity;


/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class Status extends Designation {

    public enum StatusIdentifier{

        ACTIVE_STATUS(1), PENDING_STATUS(4),
        REJECT_STATUS(8), BLOCKED_STATUS(16),
        CLOSED_STATUS(20), CONFIRM_STATUS(24);

        private final int id;

        private StatusIdentifier(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }


    public Status() {};

    public Status(int id, String name) {
       super(id,name);
    }
}
