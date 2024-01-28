package org.example.Dao;


import jakarta.persistence.*;
import org.example.Entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


public class UserDao extends GenericDao<User> implements Dao<User>{

    @Override
    public User save(User user) {
        inSession(em -> em.persist(user));
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        AtomicReference<User> user = new AtomicReference<>();
        inSession(em -> {
            Query query = em.createQuery("select u from User u where u.id = :id");
            query.setParameter("id", id);
            List<User> users = query.getResultList();
            user.set(users.stream().findFirst().orElse(null));
        });
        return Optional.ofNullable(user.get());
    }

    public List<User> findAllByUserName(String userName) {
        List<User> resultList = new ArrayList<>();
        inSession(em -> {
            TypedQuery<User> query = em.createQuery("select u from User u " +
                            "where u.userName = :userName", User.class)
                    .setParameter("userName", userName);
            resultList.addAll(query.getResultList());
        });
        return resultList;
    }

    public long countUsers(){
        AtomicLong result = new AtomicLong();
        inSession(em -> {
            Query query = em.createQuery("SELECT COUNT(u) FROM User u");
            result.set((long) query.getSingleResult());
        });
        return result.get();
    }

    @Override
    public List<User> getAll() {
        List<User> resultList = new ArrayList<>();
        inSession(em -> {
            resultList.addAll(em.createQuery("select u from User u").getResultList());
        });
        return resultList;
    }

    @Override
    public void update(Long id, User user) {
        inSession(em -> {
            Optional<User> optionalUser = findById(id);
            optionalUser.ifPresent(mergingUser -> {
                mergingUser.setUserName(user.getUserName());
                mergingUser.setPassword(user.getPassword());
                em.merge(mergingUser);
            });
        });
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<User> userOptional = findById(id);
        if (userOptional.isPresent()) {
            inSession(em -> em.remove(userOptional.get()));
            return true;
        }
        return false;
    }
}