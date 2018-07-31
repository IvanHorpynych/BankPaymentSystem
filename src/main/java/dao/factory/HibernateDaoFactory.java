package dao.factory;

import dao.abstraction.*;

import dao.impl.hibernate.*;
import entity.AccountType;
import org.hibernate.Session;


public class HibernateDaoFactory extends DaoFactory {



  public UserDao getUserDao() {
    return new HibernateUserDao();
  }


  @Override
  public CreditAccountDao getCreditAccountDao() {
    return new HibernateCreditAccountDao();
  }

  @Override
  public DepositAccountDao getDepositAccountDao() {
    return new HibernateDepositAccountDao();
  }

  @Override
  public DebitAccountDao getDebitAccountDao() {
    return new HibernateDebitAccountDao();
  }

  @Override
  public AccountsDao getAccountsDao() {
    return new HibernateAccountsDao();
  }

  @Override
  public RateDao getRateDao() {
    return new HibernateRateDao();
  }


  @Override
  public CardDao getCardDao() {
    return new HibernateCardDao();
  }

  @Override
  public PaymentDao getPaymentDao() {
    return new HibernatePaymentDao();
  }

  @Override
  public CreditRequestDao getCreditRequestDao() {
    return new HibernateCreditRequestDao();
  }

  @Override
  public GenericAccountDao getAccountDao(AccountType accountType) {
    if (accountType.getId() == AccountType.TypeIdentifier.CREDIT_TYPE.getId())
      return getCreditAccountDao();
    else if (accountType.getId() == AccountType.TypeIdentifier.DEPOSIT_TYPE.getId())
      return getDepositAccountDao();
    else if (accountType.getId() == AccountType.TypeIdentifier.DEBIT_TYPE.getId())
      return getDebitAccountDao();
    else if (accountType.getId() == AccountType.TypeIdentifier.ATM_TYPE.getId())
      return getDebitAccountDao();
    return null;
  }

}
