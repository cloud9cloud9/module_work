package org.example.security;

import lombok.*;
import org.example.Dao.UserDao;
import org.example.Entity.Account;
import org.example.Entity.Operation;
import org.example.Entity.OperationType;
import org.example.Entity.User;

import org.example.security.factory.UserStateFactory;
import org.example.security.state.AuthenticatedState;
import org.example.security.state.NotAuthenticatedState;
import org.example.security.state.UserState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserManager {
    private UserState currentState;
    private User currentUser;
    private UserDao userDao;


    @SneakyThrows
    public void logIntoTheUser() {
        currentState.logIntoTheUser();
    }

    public void logIntoAccount(Long accountId) {
        currentState.logIntoAccount(accountId);
    }

    public void performOperations() {
        currentState.performOperations();
    }

    public void createNewAccount(Account account) {
        currentState.createNewAccount(account);
    }

    public void createNewOperation(Long toAccountId, long amount, Operation operation) {
        currentState.createNewOperation(toAccountId, amount, operation);
    }

    public List<Operation> getOperationsThisAccount() {
        return currentState.getOperationsThisAccount();
    }

    public List<Operation> fetchOperationsWithAmountGreaterThan(long amount) {
        return currentState.fetchOperationsWithAmountGreaterThan(amount);
    }

    public List<Operation> filterOperationByOperationType(OperationType type) {
        return currentState.filterOperationByOperationType(type);
    }

    public List<Operation> filterOperationByCategory(OperationType.ExpenseCategory category) {
        return currentState.filterOperationByCategory(category);
    }

    public List<Operation> sortByAmount(boolean ascending) {
        return currentState.sortByAmount(ascending);
    }

    public List<Operation> findExtremeOperationOfThisAccount(boolean findMax) {
        return currentState.findExtremeOperationOfThisAccount(findMax);
    }

    public List<Operation> sortByCreatedAt(boolean ascending) {
        return currentState.sortByCreatedAt(ascending);
    }


    public List<Operation> findOperationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return currentState.findOperationsByDateRange(startDate, endDate);
    }
    public List<Account> getAllAccount(){
        return currentState.getAllAccount();
    }
    public List<Account> sortAccountByBalance(boolean ascending){
        return currentState.sortAccountByBalance(ascending);
    }
    public List<Operation> findExtremeOperationThisUser(boolean ascending){
        return currentState.findExtremeOperationThisUser(ascending);
    }
    public List<Operation> getOperationsThisUser(){
        return currentState.getOperationsThisUser();
    }

    public UserState setState() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter your password to log in:");
            String enteredPassword = scanner.nextLine();

            if ((validatePassword(enteredPassword) && authenticateUser(enteredPassword))) {
                System.out.println("Successfully logged in.");
                currentState = new AuthenticatedState(this);
            } else {
                System.out.println("Incorrect password. Login failed.");
                currentState = new NotAuthenticatedState(this);
                ((NotAuthenticatedState) currentState).logIntoTheUserWithRetry();
            }
            return currentState;
        }
    }

    public UserState setState(UserStateFactory stateFactory) {
        currentState = stateFactory.createState(this);
        return currentState;
    }

    public void setCurrentUser(Long userId) {
        try {
            Optional<User> byId = userDao.findById(userId);

            if (byId.isPresent()) {
                this.currentUser = byId.get();
            } else {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean authenticateUser(String password) {
        return this.currentUser != null && this.currentUser.getPassword().equals(password);
    }

    private boolean validatePassword(String password) {
        return !password.isEmpty() && !password.contains(" ");
    }
}
