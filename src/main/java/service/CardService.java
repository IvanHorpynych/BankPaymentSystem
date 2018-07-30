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
  private final Session session = HibernateUtil.getInstance();

  private CardService() {}

  private static class Singleton {
    private final static CardService INSTANCE = new CardService();
  }

  public static CardService getInstance() {
    return Singleton.INSTANCE;
  }

  public Card createCard(Card card) {

    CardDao cardDao = daoFactory.getCardDao(session);
    return cardDao.insert(card);
  }

  public List<Card> findAllCards() {
    CardDao cardDao = daoFactory.getCardDao(session);
    return cardDao.findAll();
  }

  public Optional<Card> findCardByNumber(long cardNumber) {
    CardDao cardDao = daoFactory.getCardDao(session);
    return cardDao.findOne(cardNumber);
  }

  public List<Card> findAllByUser(User user) {
    CardDao cardDao = daoFactory.getCardDao(session);
    return cardDao.findByUser(user);
  }

  public List<Card> findAllByAccount(Account account) {
    CardDao cardDao = daoFactory.getCardDao(session);
    return cardDao.findByAccount(account);
  }

  public void updateCardStatus(Card card, int statusId) {
    CardDao cardDao = daoFactory.getCardDao(session);
    cardDao.updateCardStatus(card, statusId);
  }


}
