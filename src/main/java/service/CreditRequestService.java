package service;

import dao.abstraction.CardDao;
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

    public List<CreditRequest> findAllCreditRequest() {
        try(DaoConnection connection = daoFactory.getConnection()) {
            CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(connection);
            return creditRequestDao.findAll();
        }
    }


    public List<CreditRequest> findAllByUser(User user) {
        try(DaoConnection connection = daoFactory.getConnection()) {
            CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(connection);
            return creditRequestDao.findByUser(user);
        }
    }

    public void updateRequestStatus(CreditRequest creditRequest) {
        try(DaoConnection connection = daoFactory.getConnection()) {
            CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(connection);
            creditRequestDao.updateRequestStatus(creditRequest);
        }
    }


}
