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
import org.example.report.strategy.CsvStrategy;
import org.example.report.strategy.UserCsvStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        User user = User.builder()
                .userName("nikol")
                .password("qwertyqwerty")
                .build();
        UserDao userDao = new UserDao();
//        userDao.save(user);

        Account account = Account.builder()
                .name("nova_post_card")
                .balance(7880)
                .user(user)
                .build();
        Account account1 = new Account();
        Account account2 = Account.builder()
                .name("other_card")
                .balance(10000)
                .user(user)
                .build();


        AccountDao accountDao = new AccountDao();
        //Optional<Account> byId = accountDao.findById(2L);
//        accountDao.save(account);

        OperationDao operationDao = new OperationDao();
        Operation operation = Operation.builder()
                .name("Buying mobilephone")
                .operationType(OperationType.EXPENSE)
                .expenseCategory(OperationType.ExpenseCategory.ENTERTAINMENT)
                .createdAt(LocalDateTime.now())
                .build();
//
//        accountDao.createNewAccount(Account.builder()
//                .balance(2500)
//                .name("nova_post_card")
//                .build(), 1L);

//        operationDao.createNewOperation(1L, 2L, 300, operation);
//        System.out.println(operationDao.getOperationsByAccountId(4L));
//        System.out.println(accountDao.getAccountByUserId(1L));
//        System.out.println("sort by amount " + operationDao.sortByAmount(1L, false));
//
//        System.out.println();
//        System.out.println("max or min" + operationDao.findExtremeOperationByAccountId(1L, true));
//        System.out.println();
        System.out.println("find extreeme" + operationDao.findExtremeOperationByAccountId(1L, true));
//        System.out.println("find extreme by userId" + operationDao.findExtremeOperationByUserId(1L, true));
//        System.out.println("filter " + operationDao.filterOperationByCategory(1L, OperationType.ExpenseCategory.MEDICINE));
//        System.out.println("filter by oper type " + operationDao.filterOperationByOperationType(1L, OperationType.EXPENSE));

        System.out.println("fatch by amout over that " + operationDao.fetchOperationsWithAmountGreaterThan(1L, 299));


        ReportService reportService = ReportService.createReportService(new UserCsvStrategy());
//        reportService.write("C:\\Users\\Vladick\\IdeaProjects\\modulework\\src" +
//                        "\\main\\resources\\user.csv",
//                userDao.getAll());
        System.out.println(reportService.read("C:\\Users\\Vladick\\IdeaProjects\\modulework\\src" +
                "\\main\\resources\\user.csv"));

    }
}