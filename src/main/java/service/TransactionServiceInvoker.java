package service;

import dao.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Supplier;

interface TransactionServiceInvoker {

  default <T> T transactionOperation(Supplier<T> transactionResultProvider) {

    /*
     * To be able to use underlying session context synchronization you need to specify session
     * context holder. Turns out that if your'e using session context holder - you should always
     * call getCurrentSession instead of openSession, as it delegates logic of obtaining AND
     * INSTANTIATING to holder, otherwise context won't be synchronized between transactions.
     * Complicated as hell.
     */

    try (Session session = HibernateUtil.getCurrentSession()) {
      // begin transaction
      Transaction transaction = session.beginTransaction();
      // your business logic
      T operationResult = transactionResultProvider.get();
      // commit changes
      transaction.commit();

      return operationResult;
    }
  }

  // with no return, approach the same as TransactionTemplate does
  default void transactionOperation(Runnable transactionOperations) {
    try (Session session = HibernateUtil.getCurrentSession()) {
      // begin transaction
      Transaction transaction = session.beginTransaction();
      // your business logic
      transactionOperations.run();
      // commit changes
      transaction.commit();
    }
  }
}
