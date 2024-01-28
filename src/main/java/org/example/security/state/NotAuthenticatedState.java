package org.example.security.state;

import lombok.SneakyThrows;
import org.example.Dao.GenericDao;
import org.example.Entity.Account;
import org.example.Entity.Operation;
import org.example.Entity.OperationType;
import org.example.Entity.User;
import org.example.Exception.AuthenticationException;
import org.example.security.UserManager;
import org.example.security.factory.AuthenticatedStateFactory;
import org.example.security.factory.BlockedStateFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class NotAuthenticatedState extends GenericDao implements UserState {
    private UserManager userManager;
    private Integer failedLoginAttempts = 0;

    public NotAuthenticatedState() {
    }



    public NotAuthenticatedState(UserManager userManager) {
        this.userManager = userManager;
    }


    @SneakyThrows
    @Override
    public void logIntoTheUser() {
        throw new AuthenticationException("Ви ввели невірний пароль");
    }

    public void logIntoTheUserWithRetry() {
        while (failedLoginAttempts < 3) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your password to log in:");
            String enteredPassword = scanner.nextLine();

            if (userManager.authenticateUser(enteredPassword)) {
                System.out.println("Successfully logged in.");
                userManager.setState(new AuthenticatedStateFactory());
                break;
            } else {
                System.out.println("Incorrect password. Retry failed login.");
                failedLoginAttempts++;
                if (failedLoginAttempts >= 3) {
                    System.out.println("Blocked due to too many failed attempts. Contact support.");
                    userManager.setState(new BlockedStateFactory());
                }
            }
        }
    }

    @Override
    public void performOperations() {
        System.out.println("Ви не можете працювати з акаунтами, оскільки ввели невірний пароль");
    }

    @SneakyThrows
    @Override
    public void logIntoAccount(Long accountId) {
        throw new AuthenticationException("Для отримання доступу до акаунту вам необхідно ввести вірний пароль юзера");
    }

    @SneakyThrows
    @Override
    public void createNewAccount(Account account) {
        throw new AuthenticationException("Для отримання доступу до акаунту вам необхідно ввести вірний пароль юзера");
    }

    @SneakyThrows
    @Override
    public void createNewOperation(Long toAccountId, long amount, Operation operation) {
        throw new AuthenticationException("Для отримання доступу до акаунту вам необхідно ввести вірний пароль юзера");
    }

    @Override
    public List<Operation> getOperationsThisAccount() {
        return null;
    }

    @Override
    public List<Operation> getOperationsThisUser() {
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

    @Override
    public List<Operation> findExtremeOperationThisUser(boolean ascending) {
        return null;
    }

    @Override
    public List<Double> getTotalBalanceOfUser() {
        return null;
    }

    @Override
    public List<Double> getTotalExpense() {
        return null;
    }

    @Override
    public List<Double> getTotalIncome() {
        return null;
    }

    @SneakyThrows
    @Override
    public void setCurrentUser(User user) {
        throw new AuthenticationException("Акаунт заблоковано!");
    }
}
