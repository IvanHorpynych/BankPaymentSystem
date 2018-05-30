package service;

import dao.abstraction.CardDao;
import dao.abstraction.CreditAccountDao;
import dao.abstraction.CreditRequestDao;
import dao.factory.DaoFactory;
import dao.factory.connection.DaoConnection;
import entity.*;

import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer.
 * Implements operations of finding, creating, deleting entities.
 * Card dao layer.
 *
 * @author JohnUkraine
 */
public class CreditRequestService {
    private final DaoFactory daoFactory= DaoFactory.getInstance();

    private CreditRequestService() {}

    private static class Singleton {
        private final static CreditRequestService INSTANCE = new CreditRequestService();
    }

    public static CreditRequestService getInstance() {
        return Singleton.INSTANCE;
    }

    public CreditRequest createRequest(CreditRequest creditRequest) {
        try(DaoConnection connection = daoFactory.getConnection()) {
            CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(connection);
            return creditRequestDao.insert(creditRequest);
        }
    }

    public List<CreditRequest> findAllPendingRequests() {
        try(DaoConnection connection = daoFactory.getConnection()) {
            CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(connection);
            return creditRequestDao.findByStatus(Status.StatusIdentifier.
                    PENDING_STATUS.getId());
        }
    }

    public Optional<CreditRequest> findCreditRequestByNumber(long requestNumber) {
        try(DaoConnection connection = daoFactory.getConnection()) {
            CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(connection);
            return creditRequestDao.findOne(requestNumber);
        }
    }


    public List<CreditRequest> findAllByUser(User user) {
        try(DaoConnection connection = daoFactory.getConnection()) {
            CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(connection);
            return creditRequestDao.findByUser(user);
        }
    }

    public void updateRequestStatus(CreditRequest creditRequest, int statusId) {
        try(DaoConnection connection = daoFactory.getConnection()) {
            CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(connection);
            creditRequestDao.updateRequestStatus(creditRequest, statusId);
        }
    }

    public void confirmRequest(CreditRequest creditRequest, CreditAccount creditAccount) {
        try(DaoConnection connection = daoFactory.getConnection()) {
            CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(connection);
            CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(connection);

            connection.startSerializableTransaction();

            creditRequestDao.updateRequestStatus(creditRequest,
                    creditRequest.getStatus().getId());

            creditAccountDao.insert(creditAccount);

            connection.commit();
        }
    }


}
