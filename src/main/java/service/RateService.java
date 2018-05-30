package service;

import dao.abstraction.RateDao;
import dao.factory.DaoFactory;
import dao.factory.connection.DaoConnection;
import entity.Rate;

import java.util.Optional;

public class RateService {

    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private RateService() {
    }

    private static class Singleton {
        private final static RateService INSTANCE = new RateService();
    }

    public static RateService getInstance() {
        return RateService.Singleton.INSTANCE;
    }

    public Optional<Rate> findValidAnnualRate(){
        try (DaoConnection connection = daoFactory.getConnection()) {
            RateDao rateDao = daoFactory.getRateDao(connection);
            return rateDao.findLast();
        }
    }

    public Rate updateAnnualRate(Rate rate){
        try (DaoConnection connection = daoFactory.getConnection()) {
            RateDao rateDao = daoFactory.getRateDao(connection);
            return rateDao.insert(rate);
        }
    }
}
