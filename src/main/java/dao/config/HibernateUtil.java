package dao.config;

import entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
  private static final SessionFactory SESSION_FACTORY;

  static {
    Configuration config =
        new Configuration().addAnnotatedClass(User.class).addAnnotatedClass(Role.class)
            .addAnnotatedClass(Status.class).addAnnotatedClass(AccountType.class)
            .addAnnotatedClass(Account.class).addAnnotatedClass(CreditAccount.class)
            .addAnnotatedClass(DepositAccount.class).addAnnotatedClass(Card.class)
            .addAnnotatedClass(Card.class).addAnnotatedClass(CreditRequest.class)
            .addAnnotatedClass(Payment.class).addAnnotatedClass(Rate.class);

    SESSION_FACTORY = config.buildSessionFactory();
  }

  public static Session getInstance() {
    return SESSION_FACTORY.openSession();
  }
}

