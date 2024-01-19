package org.example.Dao;

import jakarta.persistence.EntityManager;
import org.example.Entity.Operation;
import org.example.util.Util;
import java.util.*;


public class OperationDao extends GenericDao<Operation> implements Dao<Operation> {
    private final EntityManager em;

    public OperationDao() {
        this.em = Util.getEntityManager();
    }

    @Override
    public Operation save(Operation operation) {
        inSession(em -> em.persist(operation));
        return operation;
    }

    @Override
    public Optional<Operation> findById(Long id) {
        return Optional.ofNullable(em.find(Operation.class, id));
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
