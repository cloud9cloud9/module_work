package org.example.Dao;

import jakarta.persistence.EntityManager;
import org.example.Entity.Account;
import org.example.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDao extends GenericDao<Account> implements Dao<Account> {
    private final EntityManager em;

    public AccountDao() {
        this.em = Util.getEntityManager();
    }

    @Override
    public Account save(Account account) {
        inSession(em -> em.persist(account));
        return account;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(em.find(Account.class, id));
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
