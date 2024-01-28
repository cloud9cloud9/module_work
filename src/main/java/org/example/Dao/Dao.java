package org.example.Dao;

import jakarta.persistence.EntityManager;
import org.example.Entity.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface Dao<T> {
    void inSession(Consumer<EntityManager> entityManagerConsumer);
    T save(T elements);

    // Read
    Optional<T> findById(Long id);

    List<T> getAll();

    // Update
    void update(Long id, T elements);

    // Delete
    boolean deleteById(Long id);

}
