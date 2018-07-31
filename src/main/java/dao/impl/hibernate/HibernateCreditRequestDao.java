package dao.impl.hibernate;

import dao.abstraction.CreditRequestDao;
import dao.config.HibernateUtil;
import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HibernateCreditRequestDao implements CreditRequestDao {

  @Override
  public Optional<CreditRequest> findOne(Long requestNumber) {
    Session session = HibernateUtil.getCurrentSession();
    Query query = session.createQuery("from CreditRequest where requestNumber = :requestNumber",
        CreditRequest.class);
    query.setParameter("requestNumber", requestNumber);
    query.setMaxResults(1);
    try {
      return Optional.ofNullable((CreditRequest) query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }

  }

  @Override
  public List<CreditRequest> findAll() {
    Session session = HibernateUtil.getCurrentSession();
    Query query = session.createQuery("from CreditRequest", CreditRequest.class);
    return query.getResultList();
  }

  @Override
  public CreditRequest insert(CreditRequest request) {
    Session session = HibernateUtil.getCurrentSession();
    Objects.requireNonNull(request);
    session.beginTransaction();
    session.persist(request);
    session.getTransaction().commit();
    return request;
  }

  @Override
  public void update(CreditRequest request) {
    Session session = HibernateUtil.getCurrentSession();
    Objects.requireNonNull(request);
    session.update(request);
  }

  @Override
  public void delete(Long requestNumber) {
    Session session = HibernateUtil.getCurrentSession();
    Transaction transaction = session.beginTransaction();
    CreditRequest request = findOne(requestNumber).get();
    session.delete(request);
    transaction.commit();

  }

  @Override
  public List<CreditRequest> findByUser(User user) {
    Session session = HibernateUtil.getCurrentSession();
    Objects.requireNonNull(user);
    Query query = session.createQuery("from CreditRequest where accountHolder = :accountHolder",
        CreditRequest.class);
    query.setParameter("accountHolder", user);
    return query.getResultList();
  }

  @Override
  public List<CreditRequest> findByStatus(int statusId) {
    Session session = HibernateUtil.getCurrentSession();
    Query query =
        session.createQuery("from CreditRequest where status.id = :statusId", CreditRequest.class);
    query.setParameter("statusId", statusId);
    query.setMaxResults(1);
    return query.getResultList();
  }

  @Override
  public void updateRequestStatus(CreditRequest request, int statusId) {
    Objects.requireNonNull(request);
    request = findOne(request.getRequestNumber()).get();
    request.setStatus(new Status(statusId, "empty"));
    update(request);
  }


  /*
   * public static void main(String[] args) {
   * 
   * System.out.println("Find all:"); HibernateCreditRequestDao mySqlCreditRequestDao = new
   * HibernateCreditRequestDao(); for (CreditRequest temp : mySqlCreditRequestDao.findAll()) {
   * System.out.println(temp); }
   * 
   * System.out.println("Find one:"); System.out.println(mySqlCreditRequestDao.findOne(1L));
   * 
   * System.out.println("Find empty:"); System.out.println(mySqlCreditRequestDao.findOne(4L));
   * 
   * int random = (int) (Math.random() * 100); System.out.println("find dy user:"); User user =
   * User.newBuilder().addFirstName("first" + random).addId(3).addLastName("last" + random)
   * .addEmail("test" + random + "@com").addPassword("123").addPhoneNumber("+123") .addRole(new
   * Role(Role.RoleIdentifier.USER_ROLE.getId(), "USER")).build();
   * System.out.println(mySqlCreditRequestDao.findByUser(user));
   * 
   * System.out.println("Insert:"); CreditRequest creditRequest =
   * mySqlCreditRequestDao.insert(CreditRequest.newBuilder()
   * .addAccountHolder(user).addInterestRate(41.12f).addStatus(new Status(8, "REGECT"))
   * .addValidityDate(new Date()).addCreditLimit(BigDecimal.TEN).build());
   * 
   * System.out.println("Find all:"); for (CreditRequest temp : mySqlCreditRequestDao.findAll()) {
   * System.out.println(temp); }
   * 
   * System.out.println("update:");
   * 
   * creditRequest.setStatus(new Status(4, "PENDING")); creditRequest.setInterestRate(12);
   * mySqlCreditRequestDao.update(creditRequest);
   * 
   * System.out.println("Find one:");
   * System.out.println(mySqlCreditRequestDao.findOne(creditRequest.getRequestNumber()));
   * 
   * System.out.println("update status:"); mySqlCreditRequestDao.updateRequestStatus(creditRequest,
   * 1);
   * 
   * System.out.println("Find one:");
   * System.out.println(mySqlCreditRequestDao.findOne(creditRequest.getRequestNumber()));
   * 
   * System.out.println("delete:"); mySqlCreditRequestDao.delete(creditRequest.getRequestNumber());
   * 
   * System.out.println("Find all:"); for (CreditRequest temp : mySqlCreditRequestDao.findAll()) {
   * System.out.println(temp); }
   * 
   * }
   */
}
