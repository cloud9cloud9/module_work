package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.Exception.DaoException;

import java.util.function.Consumer;

public class Util {

    private static final EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("bank");
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    public void inSession(Consumer<EntityManager> entityManagerConsumer) {
        EntityManager em = getEntityManager();
        EntityTransaction etx = em.getTransaction();
        try {
            etx.begin();
            entityManagerConsumer.accept(em);
            etx.commit();
        } catch (Exception e) {
            etx.rollback();
            throw new DaoException("fail");
        } finally {
            em.close();
        }
    }
}
