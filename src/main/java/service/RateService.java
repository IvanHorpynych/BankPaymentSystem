package service;

import dao.abstraction.RateDao;
import dao.factory.DaoFactory;
import dao.config.HibernateUtil;
import entity.Rate;
import org.hibernate.Session;

import java.util.Optional;

public class RateService {

  private final DaoFactory daoFactory = DaoFactory.getInstance();
  private final Session session = HibernateUtil.getInstance();

  private RateService() {}

  private static class Singleton {
    private final static RateService INSTANCE = new RateService();
  }

  public static RateService getInstance() {
    return RateService.Singleton.INSTANCE;
  }

  public Optional<Rate> findValidAnnualRate() {
    RateDao rateDao = daoFactory.getRateDao(session);
    return rateDao.findLast();
  }

  public Rate updateAnnualRate(Rate rate) {
    RateDao rateDao = daoFactory.getRateDao(session);
    return rateDao.insert(rate);
  }
}
