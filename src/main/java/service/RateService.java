package service;

import dao.abstraction.RateDao;
import dao.factory.DaoFactory;
import dao.config.HibernateUtil;
import entity.Rate;
import org.hibernate.Session;

import java.util.Optional;

public class RateService {

  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private RateService() {}

  private static class Singleton {
    private final static RateService INSTANCE = new RateService();
  }

  public static RateService getInstance() {
    return RateService.Singleton.INSTANCE;
  }

  public Optional<Rate> findValidAnnualRate() {
    try(Session session = HibernateUtil.getInstance()) {
      RateDao rateDao = daoFactory.getRateDao();
      return rateDao.findLast();
    }
  }

  public Rate updateAnnualRate(Rate rate) {
    try(Session session = HibernateUtil.getInstance()) {
      RateDao rateDao = daoFactory.getRateDao();
      return rateDao.insert(rate);
    }
  }
}
