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
public class CreditRequestService implements TransactionServiceInvoker {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private CreditRequestService() {}

  private static class Singleton {
    private final static CreditRequestService INSTANCE = new CreditRequestService();
  }

  public static CreditRequestService getInstance() {
    return Singleton.INSTANCE;
  }

  public CreditRequest createRequest(CreditRequest creditRequest) {
    return transactionOperation(() -> daoFactory.getCreditRequestDao().insert(creditRequest));
  }

  public List<CreditRequest> findAllPendingRequests() {
    return transactionOperation(() -> daoFactory.getCreditRequestDao()
        .findByStatus(Status.StatusIdentifier.PENDING_STATUS.getId()));
  }

  public Optional<CreditRequest> findCreditRequestByNumber(long requestNumber) {
    return transactionOperation(() -> daoFactory.getCreditRequestDao().findOne(requestNumber));
  }


  public List<CreditRequest> findAllByUser(User user) {
    return transactionOperation(() -> daoFactory.getCreditRequestDao().findByUser(user));
  }

  public void updateRequestStatus(CreditRequest creditRequest, int statusId) {
    transactionOperation(
        () -> daoFactory.getCreditRequestDao().updateRequestStatus(creditRequest, statusId));
  }

  public void confirmRequest(CreditRequest creditRequest, CreditAccount creditAccount) {
    try (Session session = HibernateUtil.getCurrentSession()) {
      session.beginTransaction();
      CreditRequestDao creditRequestDao = daoFactory.getCreditRequestDao();
      CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao();

      creditRequestDao.updateRequestStatus(creditRequest, creditRequest.getStatus().getId());

      creditAccountDao.insert(creditAccount);

      session.getTransaction().commit();
    }
  }
}


