package org.example.Dao;

import jakarta.persistence.Query;
import org.example.Entity.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AccountDao extends GenericDao<Account> implements Dao<Account> {


    @Override
    public Account save(Account account) {
        inSession(em -> em.persist(account));
        return account;
    }

    @Override
    public Optional<Account> findById(Long id) {
        AtomicReference<Account> account = new AtomicReference<>();
        inSession(em -> {
            Query query = em.createQuery("select u from Account u where u.id = :id");
                    query.setParameter("id", id);
            List<Account> accounts = query.getResultList();
            account.set(accounts.stream().findFirst().orElse(null));
        });
        return Optional.ofNullable(account.get());
    }

    @Override
    public List<Account> getAll() {
        List<Account> accountList = new ArrayList<>();
        inSession(em -> {
            accountList.addAll(em.createQuery("select u from Account u").getResultList());
        });
        return accountList;
    }

    @Override
    public void update(Long id, Account account) {
        inSession(em -> {
            Optional<Account> optionalAccount = findById(id);
            optionalAccount.ifPresent(mergingUser -> {
                mergingUser.setUser(account.getUser());
                mergingUser.setBalance(account.getBalance());
                mergingUser.setName(account.getName());
                em.merge(mergingUser);
            });
        });
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<Account> accountOptional = findById(id);
        if (accountOptional.isPresent()) {
            inSession(em -> em.remove(accountOptional.get()));
            return true;
        }
        return false;
    }
}
