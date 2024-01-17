package org.example.Dao;

import jakarta.persistence.EntityManager;
import org.example.Entity.Account;
import org.example.Entity.Operation;
import org.example.Entity.OperationType;
import org.example.util.Util;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class OperationDao extends GenericDao<Operation> {
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

    public void createNewOperation(Long fromAccountId, Long toAccountId, long amount, Operation operation) {
        inSession(em -> {
            Account accountFrom = em.find(Account.class, fromAccountId);
            Account accountTo = em.find(Account.class, toAccountId);
            if (accountFrom.getBalance() < amount) {
                throw new RuntimeException("You dont have required amount for this operation");
            } else {
                accountFrom.setBalance(accountFrom.getBalance() - amount);
                accountTo.setBalance(accountTo.getBalance() + amount);
            }
            operation.setFromAccount(accountFrom);
            operation.setToAccount(accountTo);
            operation.setAmount(BigDecimal.valueOf(amount));
            em.persist(operation);
        });
    }

    public List<Operation> getOperationsByAccountId(Long accountId) {
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> resultList = em.createQuery("FROM Operation o WHERE o.fromAccount.id = :accountId", Operation.class)
                    .setParameter("accountId", accountId)
                    .getResultList();
            operationList.addAll(resultList);
        });
        return operationList;
    }

    public List<Operation> fetchOperationsWithAmountGreaterThan(Long accountId, long amount) {
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> operations = em.createQuery(
                            "FROM Operation o WHERE o.fromAccount.id = :accountId AND o.amount > :amount", Operation.class)
                    .setParameter("accountId", accountId)
                    .setParameter("amount", BigDecimal.valueOf(amount))
                    .getResultList();
            operationList.addAll(operations);
        });
        return operationList;
    }

    public List<Operation> filterOperationByOperationType(Long accountId, OperationType type) {
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> filterList = em.createQuery("SELECT o FROM Operation  o WHERE o.fromAccount.id = : accountId AND o.operationType =: type", Operation.class)
                    .setParameter("accountId", accountId)
                    .setParameter("type", type)
                    .getResultList();
            operationList.addAll(filterList);
        });
        return operationList;
    }

    public List<Operation> filterOperationByCategory(Long accountId, OperationType.ExpenseCategory category) {
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> filterList = em.createQuery("SELECT o FROM Operation o WHERE o.fromAccount.id = : accountId AND o.expenseCategory =: category", Operation.class)
                    .setParameter("accountId", accountId)
                    .setParameter("category", category)
                    .getResultList();
            operationList.addAll(filterList);
        });
        return operationList;
    }

    // крутий метод, мені дуже сподобалась реалізація)
    public List<Operation> sortByAmount(Long accountId, boolean ascending) {
        List<Operation> operationList = getOperationsByAccountId(accountId);
        inSession(em -> {
            Comparator<Operation> comparator = ascending ?
                    Comparator.comparing(Operation::getAmount) :
                    Comparator.comparing(Operation::getAmount).reversed();

            Collections.sort(operationList, comparator);
        });
        return operationList;
    }
    public List<Operation> findExtremeOperationByAccountId(Long accountId, boolean findMax){
        String queryString = findMax ?
                "SELECT o FROM Operation o WHERE o.fromAccount.id = :accountId ORDER BY o.amount DESC" :
                "SELECT o FROM Operation o WHERE o.fromAccount.id = :accountId ORDER BY o.amount ASC";
        List<Operation> operationList = new ArrayList<>();
                inSession(em -> {
                    List<Operation> operations = em.createQuery(queryString, Operation.class)
                            .setParameter("accountId", accountId)
                            .setMaxResults(1)
                            .getResultList();
                    operationList.addAll(operations);
        });
                return operationList;
    }
    public List<Operation> sortByCreatedAt(Long accountId, boolean ascending){
        String queryString = ascending ?
                "SELECT o FROM Operation o WHERE o.fromAccount.id = :accountId ORDER BY o.createdAt DESC" :
                "SELECT o FROM Operation o WHERE o.fromAccount.id = :accountId ORDER BY o.createdAt ASC";
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> operations = em.createQuery(queryString, Operation.class)
                    .setParameter("accountId", accountId)
                    .getResultList();
            operationList.addAll(operations);
        });
        return operationList;
    }
    public Operation findExtremeOperationByUserId(Long userId, boolean ascending){
        AtomicReference<Operation> extremeOperation = new AtomicReference<>();
        inSession(em -> {
            List<Account> accounts = em.createQuery("SELECT o FROM Account o WHERE o.user.id = :userId", Account.class)
                    .setParameter("userId", userId)
                    .getResultList();
            for(Account account : accounts){
                String queryString = ascending ?
                        "SELECT o FROM Operation o WHERE o.fromAccount = :account ORDER BY o.amount DESC" :
                        "SELECT o FROM Operation o WHERE o.fromAccount = :account ORDER BY o.amount ASC";
                List<Operation> operations = em.createQuery(queryString, Operation.class)
                        .setParameter("account", account)
                        .setMaxResults(1)
                        .getResultList();
                extremeOperation.set(operations.get(0));
            }
        });
        return extremeOperation.get();
    }
}
