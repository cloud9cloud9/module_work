package org.example;

import jdk.swing.interop.SwingInterOpUtils;
import org.example.Dao.AccountDao;
import org.example.Dao.OperationDao;
import org.example.Dao.UserDao;
import org.example.Entity.Account;
import org.example.Entity.Operation;
import org.example.Entity.OperationType;
import org.example.Entity.User;
import org.example.report.ReportService;
import org.example.report.strategy.AccountCsvStrategy;
import org.example.report.strategy.CsvStrategy;
import org.example.report.strategy.OperationCsvStrategy;
import org.example.report.strategy.UserCsvStrategy;
import org.example.security.UserManager;

import org.example.security.state.NotAuthenticatedState;
import org.example.security.state.UserState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

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

        Account account3 = Account.builder().balance(1000).name("new_card").build();


        UserManager userManager = UserManager.builder().userDao(userDao).build();
        userManager.setCurrentUser(1L);
        UserState userState = userManager.setState();
        userManager.sortAccountByBalance(true);
    }
}