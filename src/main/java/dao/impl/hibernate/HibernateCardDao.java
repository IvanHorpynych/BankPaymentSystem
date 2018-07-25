
package dao.impl.hibernate;

import dao.abstraction.CardDao;
import dao.datasource.PooledConnection;
import dao.hibernate.HibernateUtil;
import dao.impl.mysql.DefaultDaoImpl;
import dao.impl.mysql.converter.CardDtoConverter;
import dao.impl.mysql.converter.DtoConverter;
import dao.util.time.TimeConverter;
import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


/**
 * Created by JohnUkraine on 5/07/2018.
 */

public class HibernateCardDao implements CardDao {


  @Override
  public Optional<Card> findOne(Long cardNumber) {
    try (Session session = HibernateUtil.getInstance()) {
      Query query = session.createQuery("from Card where cardNumber = :cardNumber", Card.class);
      query.setParameter("cardNumber", cardNumber);
      query.setMaxResults(1);
      try {
        return Optional.ofNullable((Card) query.getSingleResult());
      } catch (NoResultException e) {
        return Optional.empty();
      }

    }
  }

  @Override
  public List<Card> findAll() {
    try (Session session = HibernateUtil.getInstance()) {
      Query query = session.createQuery("from Card ", Card.class);
      return query.getResultList();
    }
  }

  @Override
  public Card insert(Card card) {
    Objects.requireNonNull(card);
    try (Session session = HibernateUtil.getInstance()) {
      session.beginTransaction();
      System.out.println(card);
      session.persist(card);
      System.out.println(card);
      session.getTransaction().commit();
      return card;
    }
  }

  @Override
  public void update(Card card) {
    Objects.requireNonNull(card);
    try (Session session = HibernateUtil.getInstance()) {
      Transaction transaction = session.beginTransaction();
      session.update(card);
      transaction.commit();
    }
  }

  @Override
  public void delete(Long cardNumber) {
    try (Session session = HibernateUtil.getInstance()) {
      Transaction transaction = session.beginTransaction();
      Card card = findOne(cardNumber).get();
      session.delete(card);
      transaction.commit();
    }

  }

  @Override
  public List<Card> findByUser(User user) {
    Objects.requireNonNull(user);
    try (Session session = HibernateUtil.getInstance()) {
      Query query = session
          .createQuery("from Card where account.accountHolder.id = :accountHolderId", Card.class);
      query.setParameter("accountHolderId", user.getId());
      query.setMaxResults(1);
      return query.getResultList();
    }
  }

  @Override
  public List<Card> findByAccount(Account account) {
    try (Session session = HibernateUtil.getInstance()) {
      Query query = session.createQuery("from Card where account.id = :accountId", Card.class);
      query.setParameter("accountId", account.getAccountNumber());
      query.setMaxResults(1);
      return query.getResultList();
    }
  }


  @Override
  public List<Card> findByUserAndStatus(User user, Status status) {
    try (Session session = HibernateUtil.getInstance()) {
      Query query = session.createQuery(
          "from Card where account.accountHolder.id = :accountHolderId and status.id = :statusId",
          Card.class);
      query.setParameter("accountHolderId", user.getId());
      query.setParameter("statusId", status.getId());

      query.setMaxResults(1);
      return query.getResultList();
    }
  }

  @Override
  public void updateCardStatus(Card card, int statusId) {
    Objects.requireNonNull(card);

    card = findOne(card.getCardNumber()).get();
    card.setStatus(new Status(statusId, "empty"));
    update(card);
  }


  public static void main(String[] args) {

    User user = User.newBuilder().addFirstName("first").addId(3).addLastName("last")
        .addEmail("test@com").addPassword("123").addPhoneNumber("+123")
        .addRole(new Role(Role.RoleIdentifier.USER_ROLE.getId(), "USER")).build();

    Account debitAccount = Account.newBuilder().addAccountNumber(2).addAccountHolder(user)
        .addAccountType(new AccountType(16, "DEBIT")).addBalance(BigDecimal.ONE)
        .addStatus(new Status(1, "ACTIVE")).build();

    System.out.println("Find all:");
    HibernateCardDao mySqlCardDao = new HibernateCardDao();
    ((HibernateCardDao) mySqlCardDao).printCard(mySqlCardDao.findAll());

    int random = (int) (Math.random() * 100);

    System.out.println("Find one:");
    System.out.println(mySqlCardDao.findOne(1000000000000000L));

    System.out.println("find dy user:");
    System.out.println(mySqlCardDao.findByUser(user));

    System.out.println("Find by account");
    for (Card card : mySqlCardDao.findByAccount(debitAccount)) {
      System.out.println(card);
    }

    System.out.println("Insert:");
    Card card = mySqlCardDao.insert(Card.newBuilder().addAccount(debitAccount).addPin(1111)
        .addCvv(444).addExpireDate(new Date()).addType(Card.CardType.MASTERCARD)
        .addStatus(new Status(1, "ACTIVE")).build());
    List<Card> temp = new ArrayList<>();
    card = mySqlCardDao.findOne(card.getCardNumber()).get();
    temp.add(card);
    System.out.println("Find one:");
    ((HibernateCardDao) mySqlCardDao).printCard(temp);

    System.out.println("Find by account");
    for (Card temp1 : mySqlCardDao.findByAccount(debitAccount)) {
      System.out.println(temp1);
    }

    System.out.println("update:");
    card.setCvv(333);
    card.setType(Card.CardType.VISA);
    mySqlCardDao.update(card);

    temp.clear();
    card = mySqlCardDao.findOne(card.getCardNumber()).get();
    temp.add(card);
    System.out.println("Find one:");
    ((HibernateCardDao) mySqlCardDao).printCard(temp);

    System.out.println("Find by user and status:");
    ((HibernateCardDao) mySqlCardDao)
        .printCard(mySqlCardDao.findByUserAndStatus(user, new Status(1, "ACTIVE")));

    System.out.println("Delete:");
    mySqlCardDao.delete(card.getCardNumber());
    ((HibernateCardDao) mySqlCardDao).printCard(mySqlCardDao.findAll());

  }

  protected void printCard(List<Card> list) {
    for (Card card : list) {
      System.out.println("Card: " + card);
      System.out.println();
    }
  }
}
