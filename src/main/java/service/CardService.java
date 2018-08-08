package service;

import dao.factory.DaoFactory;
import entity.Account;
import entity.Card;
import entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer. Implements operations of finding,
 * creating, deleting entities. Card dao layer.
 *
 * @author JohnUkraine
 */
public class CardService implements TransactionServiceInvoker {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private CardService() {}

  private static class Singleton {
    private final static CardService INSTANCE = new CardService();
  }

  public static CardService getInstance() {
    return Singleton.INSTANCE;
  }

  public Card createCard(Card card) {
    return transactionOperation(() -> daoFactory.getCardDao().insert(card));
  }

  public List<Card> findAllCards() {
    return transactionOperation(() -> daoFactory.getCardDao().findAll());
  }

  public Optional<Card> findCardByNumber(long cardNumber) {
    return transactionOperation(() -> daoFactory.getCardDao().findOne(cardNumber));
  }

  public List<Card> findAllByUser(User user) {
    return transactionOperation(() -> daoFactory.getCardDao().findByUser(user));
  }

  public List<Card> findAllByAccount(Account account) {
    return transactionOperation(() -> daoFactory.getCardDao().findByAccount(account));
  }

  public void updateCardStatus(Card card, int statusId) {
    transactionOperation(() -> daoFactory.getCardDao().updateCardStatus(card, statusId));
  }


}
