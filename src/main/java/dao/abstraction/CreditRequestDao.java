package dao.abstraction;

import entity.CreditRequest;
import entity.Status;
import entity.User;

import java.util.List;

public interface CreditRequestDao extends GenericDao<CreditRequest, Long> {
    /**
     * Retrieves all credit request associated with certain user.
     *
     * @param user user to retrieve accounts related with him
     * @return list of retrieved accounts
     */
    List<CreditRequest> findByUser(User user);

    /**
     * Retrieves all credit request associated with certain status.
     *
     * @param statusId status of account
     * @return list of retrieved accounts
     */
    List<CreditRequest> findByStatus(long statusId);

    /**
     * Updates certain credit request status.
     *
     * @param request account which status will be updated.
     */
    void updateRequestStatus(CreditRequest request, int statusId);

}
