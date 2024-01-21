package org.example;

import org.example.Dao.AccountDao;
import org.example.Dao.OperationDao;
import org.example.Dao.UserDao;
import org.example.Entity.Account;
import org.example.Entity.Operation;
import org.example.Entity.OperationType;
import org.example.security.UserManager;
import org.example.security.state.UserState;

import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) {

        AccountDao accountDao = new AccountDao();
        UserDao userDao = new UserDao();
        OperationDao operationDao = new OperationDao();

        Operation operation = Operation.builder()
                .name("Buying mobilephone")
                .operationType(OperationType.EXPENSE)
                .expenseCategory(OperationType.ExpenseCategory.ENTERTAINMENT)
                .createdAt(LocalDateTime.now())
                .build();

        Account account3 = Account.builder().balance(10000).name("new_card222").build();

        UserManager userManager = UserManager.builder().userDao(userDao).build();
        userManager.setCurrentUser(2L);
        UserState userState = userManager.setState();
        userState.setCurrentUser(userManager.getCurrentUser());
        userManager.logIntoAccount(1L);
        userManager.getOperationsThisUser();
        //qwertyqwerty
        userManager.getTotalIncome();
    }
}