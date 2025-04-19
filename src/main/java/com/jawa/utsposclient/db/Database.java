package com.jawa.utsposclient.db;

import com.jawa.utsposclient.entities.*;
import com.jawa.utsposclient.utils.Config;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

public class Database {
    private static SessionFactory sessionFactory;

    public static void init() {
        if(sessionFactory == null) {
            sessionFactory = buildSessionFactory();
            Seeder.seedAdmin();
        }
    }

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration config = new Configuration();

            Properties settings = new Properties();
            settings.put(Environment.JAKARTA_JDBC_DRIVER, Config.getProperty("hibernate.driver"));
            settings.put(Environment.JAKARTA_JDBC_URL, Config.getProperty("hibernate.url"));
            settings.put(Environment.JAKARTA_JDBC_USER, Config.getProperty("hibernate.username"));
            settings.put(Environment.JAKARTA_JDBC_PASSWORD, Config.getProperty("hibernate.password"));
            settings.put(Environment.SHOW_SQL, Config.getProperty("hibernate.show_sql"));
            settings.put(Environment.HBM2DDL_AUTO, Config.getProperty("hibernate.hbm2ddl.auto"));

            config.setProperties(settings);

            List<Class<?>> entities = List.of(
                Users.class,
                Products.class,
                PerishableProducts.class,
                NonPerishableProducts.class,
                BundleProducts.class,
                BundleItems.class,
                DigitalProducts.class,
                Transactions.class,
                TransactionItems.class,
                PurchaseTransactions.class,
                RefundTransactions.class,
                UserSessions.class
            );

            entities.forEach(config::addAnnotatedClass);

            return config.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Error building session factory");
            throw e;
        }
    }

    public static <R> R executeTransaction(Function<Session, R> action) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result;
            try {
                result = action.apply(session);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
            return result;
        }
    }

    public static void executeVoidTransaction(Consumer<Session> action) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                action.accept(session);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }
}
