package org.example.Dao;

import jakarta.persistence.Query;
import org.example.Entity.Operation;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


public class OperationDao extends GenericDao<Operation> implements Dao<Operation> {

    @Override
    public Operation save(Operation operation) {
        inSession(em -> em.persist(operation));
        return operation;
    }

    @Override
    public Optional<Operation> findById(Long id) {
        AtomicReference<Operation> operation = new AtomicReference<>();
        inSession(em -> {
            Query query = em.createQuery("select u from Operation u where u.fromAccount.id = :id");
            query.setParameter("id", id);
            List<Operation> operations = query.getResultList();
            operation.set(operations.stream().findFirst().orElse(null));
        });
        return Optional.ofNullable(operation.get());
    }

    @Override
    public List<Operation> getAll() {
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            operationList.addAll(em.createQuery("select u from Operation u").getResultList());
        });
        return operationList;
    }

    @Override
    public void update(Long id, Operation operation) {
        inSession(em -> {
            Optional<Operation> optionalUser = findById(id);
            optionalUser.ifPresent(mergingUser -> {
                mergingUser.setName(operation.getName());
                mergingUser.setOperationType(operation.getOperationType());
                mergingUser.setExpenseCategory(operation.getExpenseCategory());
                mergingUser.setCreatedAt(operation.getCreatedAt());
                mergingUser.setAmount(operation.getAmount());
                mergingUser.setFromAccount(operation.getFromAccount());
                mergingUser.setToAccount(operation.getToAccount());
                em.merge(mergingUser);
            });
        });
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<Operation> operationOptional = findById(id);
        if (operationOptional.isPresent()) {
            inSession(em -> em.remove(operationOptional.get()));
            return true;
        }
        return false;
    }
}
