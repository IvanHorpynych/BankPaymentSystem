package dao.abstraction;

import entity.Rate;
import entity.Status;

import java.util.Optional;

public interface RateDao extends GenericDao<Rate, Long> {

    /**
     * Retrieve last rate from database
     * @return optional, which contains retrieved object or null
     */
    Optional<Rate> findLast();
}
