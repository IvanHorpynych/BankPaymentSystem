package dao.factory;

import dao.abstraction.*;

import dao.impl.hibernate.*;
import entity.AccountType;
import org.hibernate.Session;


public class HibernateDaoFactory extends DaoFactory {



  public UserDao getUserDao(Session session) {
    return new HibernateUserDao(session);
  }


  @Override
  public CreditAccountDao getCreditAccountDao(Session session) {
    return new HibernateCreditAccountDao(session);
  }

  @Override
  public DepositAccountDao getDepositAccountDao(Session session) {
    return new HibernateDepositAccountDao(session);
  }

  @Override
  public DebitAccountDao getDebitAccountDao(Session session) {
    return new HibernateDebitAccountDao(session);
  }

  @Override
  public AccountsDao getAccountsDao(Session session) {
    return new HibernateAccountsDao(session);
  }

  @Override
  public RateDao getRateDao(Session session) {
    return new HibernateRateDao(session);
  }


  @Override
  public CardDao getCardDao(Session session) {
    return new HibernateCardDao(session);
  }

  @Override
  public PaymentDao getPaymentDao(Session session) {
    return new HibernatePaymentDao(session);
  }

  @Override
  public CreditRequestDao getCreditRequestDao(Session session) {
    return new HibernateCreditRequestDao(session);
  }

  @Override
  public GenericAccountDao getAccountDao(Session session, AccountType accountType) {
    if (accountType.getId() == AccountType.TypeIdentifier.CREDIT_TYPE.getId())
      return getCreditAccountDao(session);
    else if (accountType.getId() == AccountType.TypeIdentifier.DEPOSIT_TYPE.getId())
      return getDepositAccountDao(session);
    else if (accountType.getId() == AccountType.TypeIdentifier.DEBIT_TYPE.getId())
      return getDebitAccountDao(session);
    else if (accountType.getId() == AccountType.TypeIdentifier.ATM_TYPE.getId())
      return getDebitAccountDao(session);
    return null;
  }

}
