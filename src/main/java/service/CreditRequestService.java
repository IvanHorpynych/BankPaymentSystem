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
  private final Session session = HibernateUtil.getInstance();

  private CreditRequestService() {}

  private static class Singleton {
    private final static CreditRequestService INSTANCE = new CreditRequestService();
  }

  public static CreditRequestService getInstance() {
    return Singleton.INSTANCE;
  }

  public CreditRequest createRequest(CreditRequest creditRequest) {
    CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(session);
    return creditRequestDao.insert(creditRequest);
  }

  public List<CreditRequest> findAllPendingRequests() {
    CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(session);
    return creditRequestDao.findByStatus(Status.StatusIdentifier.PENDING_STATUS.getId());
  }

  public Optional<CreditRequest> findCreditRequestByNumber(long requestNumber) {
    CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(session);
    return creditRequestDao.findOne(requestNumber);
  }


  public List<CreditRequest> findAllByUser(User user) {
    CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(session);
    return creditRequestDao.findByUser(user);
  }

  public void updateRequestStatus(CreditRequest creditRequest, int statusId) {
    CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(session);
    creditRequestDao.updateRequestStatus(creditRequest, statusId);
  }

  public void confirmRequest(CreditRequest creditRequest, CreditAccount creditAccount) {
    CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao(session);
    CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(session);

    session.beginTransaction();

    creditRequestDao.updateRequestStatus(creditRequest, creditRequest.getStatus().getId());

    creditAccountDao.insert(creditAccount);

    session.getTransaction().commit();
  }
}


