package org.example.security.state;

import org.example.Entity.Account;
import org.example.Entity.Operation;
import org.example.Entity.OperationType;
import org.example.Entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserState {
    void logIntoTheUser();
    void setCurrentUser(User user);
    void performOperations();
    void logIntoAccount(Long accountId);
    void createNewAccount(Account account);
    void createNewOperation(Long toAccountId, long amount, Operation operation);
    List<Operation> getOperationsThisAccount();
    List<Operation> getOperationsThisUser();
    List<Operation> fetchOperationsWithAmountGreaterThan(long amount);
    List<Operation> filterOperationByOperationType(OperationType type);
    List<Operation> filterOperationByCategory(OperationType.ExpenseCategory category);
    List<Operation> sortByAmount(boolean ascending);
    List<Operation> findExtremeOperationOfThisAccount(boolean findMax);
    List<Operation> sortByCreatedAt(boolean ascending);
    List<Operation> findOperationsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Account> getAllAccount();
    List<Account> sortAccountByBalance(boolean ascending);
    List<Operation> findExtremeOperationThisUser(boolean ascending);
    List<Double> getTotalBalanceOfUser();
    List<Double> getTotalExpense();
    List<Double> getTotalIncome();

}
