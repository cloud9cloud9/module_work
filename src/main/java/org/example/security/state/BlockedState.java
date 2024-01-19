package org.example.security.state;

import lombok.SneakyThrows;
import org.example.Entity.Account;
import org.example.Entity.Operation;
import org.example.Entity.OperationType;
import org.example.Entity.User;
import org.example.Exception.AuthenticationException;

import java.time.LocalDateTime;
import java.util.List;

public class BlockedState implements UserState {
    @Override
    public void logIntoTheUser() {
        System.out.println("Акаунт заблокований. Зверніться до служби підтримки.");
    }

    @Override
    public void performOperations() {
        System.out.println("Акаунт заблокований. Зверніться до служби підтримки.");
    }

    @Override
    public void logIntoAccount(Long accountId) {
        System.out.println("Акаунт заблокований!");
    }

    @SneakyThrows
    @Override
    public void createNewAccount(Account account) {
        throw new AuthenticationException("Акаунт заблоковано!");
    }
    @SneakyThrows
    @Override
    public void createNewOperation(Long toAccountId, long amount, Operation operation) {
        throw new AuthenticationException("Акаунт заблоковано!");
    }

    @Override
    public List<Operation> getOperationsThisAccount() {
        return null;
    }

    @Override
    public List<Operation> fetchOperationsWithAmountGreaterThan(long amount) {
        return null;
    }

    @Override
    public List<Operation> filterOperationByOperationType(OperationType type) {
        return null;
    }

    @Override
    public List<Operation> filterOperationByCategory(OperationType.ExpenseCategory category) {
        return null;
    }

    @Override
    public List<Operation> sortByAmount(boolean ascending) {
        return null;
    }

    @Override
    public List<Operation> findExtremeOperationOfThisAccount(boolean findMax) {
        return null;
    }

    @Override
    public List<Operation> sortByCreatedAt(boolean ascending) {
        return null;
    }

    @Override
    public List<Operation> findOperationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public List<Account> getAllAccount() {
        return null;
    }

    @Override
    public List<Account> sortAccountByBalance(boolean ascending) {
        return null;
    }

}
