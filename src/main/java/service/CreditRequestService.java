package service;

import dao.abstraction.CreditAccountDao;
import dao.abstraction.CreditRequestDao;
import dao.factory.DaoFactory;
import dao.config.HibernateUtil;
import entity.*;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer. Implements operations of finding,
 * creating, deleting entities. Card dao layer.
 *
 * @author JohnUkraine
 */
public class CreditRequestService {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private CreditRequestService() {}

  private static class Singleton {
    private final static CreditRequestService INSTANCE = new CreditRequestService();
  }

  public static CreditRequestService getInstance() {
    return Singleton.INSTANCE;
  }

  public CreditRequest createRequest(CreditRequest creditRequest) {
    try(Session session = HibernateUtil.getInstance()) {
      CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao();
      return creditRequestDao.insert(creditRequest);
    }
  }

  public List<CreditRequest> findAllPendingRequests() {
    try(Session session = HibernateUtil.getInstance()) {
      CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao();
      return creditRequestDao.findByStatus(Status.StatusIdentifier.PENDING_STATUS.getId());
    }
  }

  public Optional<CreditRequest> findCreditRequestByNumber(long requestNumber) {
    try(Session session = HibernateUtil.getInstance()) {
      CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao();
      return creditRequestDao.findOne(requestNumber);
    }
  }


  public List<CreditRequest> findAllByUser(User user) {
    try(Session session = HibernateUtil.getInstance()) {
      CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao();
      return creditRequestDao.findByUser(user);
    }
  }

  public void updateRequestStatus(CreditRequest creditRequest, int statusId) {
    try(Session session = HibernateUtil.getInstance()) {
      CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao();
      creditRequestDao.updateRequestStatus(creditRequest, statusId);
    }
  }

  public void confirmRequest(CreditRequest creditRequest, CreditAccount creditAccount) {
    try(Session session = HibernateUtil.getInstance()) {
      session.beginTransaction();
      CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao();
      CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao();

      creditRequestDao.updateRequestStatus(creditRequest, creditRequest.getStatus().getId());

      creditAccountDao.insert(creditAccount);

      session.getTransaction().commit();
    }
  }
}


