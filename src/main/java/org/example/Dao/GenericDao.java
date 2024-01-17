package org.example.Dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.Exception.DaoException;

import java.util.function.Consumer;

import static org.example.util.Util.getEntityManager;

public abstract class GenericDao<T> implements Dao<T> {
    private final EntityManager em;

    public GenericDao() {
        this.em = getEntityManager();
    }

    public void inSession(Consumer<EntityManager> entityManagerConsumer) {
        EntityTransaction etx = em.getTransaction();
        try {
            etx.begin();
            entityManagerConsumer.accept(em);
            etx.commit();
        } catch (Exception e) {
            etx.rollback();
            throw new RuntimeException();
        }
    }
}
