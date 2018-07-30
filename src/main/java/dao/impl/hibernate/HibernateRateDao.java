package dao.impl.hibernate;

import dao.abstraction.RateDao;
import dao.config.HibernateUtil;
import dao.factory.HibernateDaoFactory;
import entity.Rate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Created by JohnUkraine on 5/07/2018.
 */

public class HibernateRateDao implements RateDao {

  private Session session;

  public HibernateRateDao(Session session) {
    this.session = session;
  }


  @Override
  public Optional<Rate> findOne(Long number) {
    Query query = session.createQuery("from Rate where id = :number", Rate.class);
    query.setParameter("number", number);
    query.setMaxResults(1);
    try {
      return Optional.ofNullable((Rate) query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }

  }

  @Override
  public List<Rate> findAll() {
    Query query = session.createQuery("from Rate ", Rate.class);
    return query.getResultList();
  }

  @Override
  public Rate insert(Rate rate) {
    Objects.requireNonNull(rate);
    session.beginTransaction();
    session.persist(rate);
    session.getTransaction().commit();
    return rate;
  }

  @Override
  public void update(Rate rate) {
    Objects.requireNonNull(rate);
    Transaction transaction = session.beginTransaction();
    session.update(rate);
    transaction.commit();
  }

  @Override
  public void delete(Long cardNumber) {
    Transaction transaction = session.beginTransaction();
    Rate rate = findOne(cardNumber).get();
    session.delete(rate);
    transaction.commit();

  }

  @Override
  public Optional<Rate> findLast() {
    Query query = session.createQuery("from Rate order by createdTime desc", Rate.class);
    query.setMaxResults(1);
    return Optional.ofNullable((Rate) query.getSingleResult());
  }



  /*
   * public static void main(String[] args) {
   * 
   * HibernateRateDao mySqlRateDao = new HibernateRateDao(HibernateUtil.getInstance());
   * mySqlRateDao.printAll(mySqlRateDao.findAll()); System.out.println();
   * 
   * System.out.println("Find one with id 1:"); System.out.println(mySqlRateDao.findOne(1L));
   * 
   * 
   * System.out.println("Insert test:"); Rate accountType = mySqlRateDao. insert(new Rate(13.4f, new
   * Date())); mySqlRateDao.printAll(mySqlRateDao.findAll()); System.out.println("Last:");
   * System.out.println(mySqlRateDao.findLast());
   * 
   * 
   * }
   * 
   * protected void printAll(List<Rate> list) { System.out.println("Find all:"); for (Rate type :
   * list) { System.out.println(type); } }
   * 
   * 
   */

}
