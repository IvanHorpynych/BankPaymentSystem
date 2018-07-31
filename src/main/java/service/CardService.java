package service;

import dao.abstraction.CardDao;
import dao.factory.DaoFactory;
import dao.config.HibernateUtil;
import entity.Account;
import entity.Card;
import entity.User;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer. Implements operations of finding,
 * creating, deleting entities. Card dao layer.
 *
 * @author JohnUkraine
 */
public class CardService {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private CardService() {}

  private static class Singleton {
    private final static CardService INSTANCE = new CardService();
  }

  public static CardService getInstance() {
    return Singleton.INSTANCE;
  }

  public Card createCard(Card card) {
    try(Session session = HibernateUtil.getInstance()) {
      CardDao cardDao = daoFactory.getCardDao();
      return cardDao.insert(card);
    }
  }

  public List<Card> findAllCards() {
    try(Session session = HibernateUtil.getInstance()) {
      CardDao cardDao = daoFactory.getCardDao();
      return cardDao.findAll();
    }
  }

  public Optional<Card> findCardByNumber(long cardNumber) {
    try(Session session = HibernateUtil.getInstance()) {
      CardDao cardDao = daoFactory.getCardDao();
      return cardDao.findOne(cardNumber);
    }
  }

  public List<Card> findAllByUser(User user) {
    try(Session session = HibernateUtil.getInstance()) {
      CardDao cardDao = daoFactory.getCardDao();
      return cardDao.findByUser(user);
    }
  }

  public List<Card> findAllByAccount(Account account) {
    try(Session session = HibernateUtil.getInstance()) {
      CardDao cardDao = daoFactory.getCardDao();
      return cardDao.findByAccount(account);
    }
  }

  public void updateCardStatus(Card card, int statusId) {
    try(Session session = HibernateUtil.getInstance()) {
      CardDao cardDao = daoFactory.getCardDao();
      cardDao.updateCardStatus(card, statusId);
    }
  }


}
