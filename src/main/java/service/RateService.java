package service;

import dao.factory.DaoFactory;
import entity.Rate;

import java.util.Optional;

public class RateService implements TransactionServiceInvoker {

  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private RateService() {}

  private static class Singleton {
    private final static RateService INSTANCE = new RateService();
  }

  public static RateService getInstance() {
    return RateService.Singleton.INSTANCE;
  }

  public Optional<Rate> findValidAnnualRate() {
    return transactionOperation(() -> daoFactory.getRateDao().findLast());
  }

  public Rate updateAnnualRate(Rate rate) {
    return transactionOperation(() -> daoFactory.getRateDao().insert(rate));
  }
}
