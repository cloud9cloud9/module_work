package org.example.security.state;

import org.example.Dao.GenericDao;
import org.example.Entity.Account;
import org.example.Entity.Operation;
import org.example.Entity.OperationType;
import org.example.Entity.User;
import org.example.report.ReportService;
import org.example.report.strategy.AccountCsvStrategy;
import org.example.report.strategy.OperationCsvStrategy;
import org.example.report.strategy.UserCsvStrategy;
import org.example.security.UserManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


public class AuthenticatedState extends GenericDao<User> implements UserState {
    private ReportService<User> userReportService = ReportService.createReportService(new UserCsvStrategy());
    private ReportService<Operation> operationReportService = ReportService.createReportService(new OperationCsvStrategy());
    private ReportService<Account> accountReportService = ReportService.createReportService(new AccountCsvStrategy());

    private Long selectedUserId;
    private UserManager userManager;
    private Long selectedAccountId;

    public AuthenticatedState(UserManager userManager) {
        this.userManager = userManager;
        this.selectedUserId = userManager.getCurrentUser().getId();
    }

    public AuthenticatedState() {
    }

    @Override
    public void logIntoTheUser() {
        System.out.println("Ви успішно увійшли у акаунт");
    }

    @Override
    public void setCurrentUser(User user) {
        selectedUserId = user.getId();
    }

    @Override
    public void performOperations() {
        System.out.println("Ви успішно увійшли у акаунт");
    }


    @Override
    public void logIntoAccount(Long accountId) {
        AtomicReference<Optional<Account>> accountOptional = new AtomicReference<>();
        inSession(em -> {
            accountOptional.set(Optional.ofNullable(em.find(Account.class, accountId)));
        });

        if (accountOptional.get().isPresent()) {
            selectedAccountId = accountId;
            System.out.println("Ви успішно увійшли у конкретний акаунт: " + accountOptional.get());
        } else {
            System.out.println("Акаунт не знайдено.");
        }
    }

    @Override
    public void createNewAccount(Account account) {
        inSession(em -> {
            User user = em.find(User.class, selectedUserId);
            account.setUser(user);
            em.persist(account);
            System.out.println("Успішно створено нову карту!");
        });
    }

    @Override
    public void createNewOperation(Long toAccountId, long amount, Operation operation) {
        inSession(em -> {
            Account accountFrom = em.find(Account.class, selectedAccountId);
            Account accountTo = em.find(Account.class, toAccountId);
            if (accountFrom.getBalance() < amount) {
                throw new RuntimeException("You dont have required amount for this operation");
            } else {
                accountFrom.setBalance(accountFrom.getBalance() - amount);
                accountTo.setBalance(accountTo.getBalance() + amount);
            }
            if (operation.getOperationType() == null) {
                throw new RuntimeException("Operation type cannot be null");
            }
            operation.setFromAccount(accountFrom);
            operation.setToAccount(accountTo);
            operation.setAmount(BigDecimal.valueOf(amount));
            em.persist(operation);
        });
    }

    @Override
    public List<Operation> getOperationsThisAccount() {
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> resultList = em.createQuery("FROM Operation o WHERE o.fromAccount.id = :accountId", Operation.class)
                    .setParameter("accountId", selectedAccountId)
                    .getResultList();
            operationList.addAll(resultList);
        });
        operationReportService.write(operationList);
        return operationList;
    }

    @Override
    public List<Operation> getOperationsThisUser() {
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Account> accounts = em.createQuery("SELECT a FROM Account a WHERE a.user.id = :userId", Account.class)
                    .setParameter("userId", selectedUserId)
                    .getResultList();

            for (Account account : accounts) {
                List<Operation> operations = em.createQuery("SELECT o FROM Operation o WHERE o.fromAccount = :account", Operation.class)
                        .setParameter("account", account)
                        .getResultList();
                operationList.addAll(operations);
            }
        });
        operationReportService.write(operationList);
        return operationList;
    }

    @Override
    public List<Operation> fetchOperationsWithAmountGreaterThan(long amount) {
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> operations = em.createQuery(
                            "FROM Operation o WHERE o.fromAccount.id = :accountId AND o.amount > :amount", Operation.class)
                    .setParameter("accountId", selectedAccountId)
                    .setParameter("amount", BigDecimal.valueOf(amount))
                    .getResultList();
            operationList.addAll(operations);
        });
        operationReportService.write(operationList);
        return operationList;
    }

    @Override
    public List<Operation> filterOperationByOperationType(OperationType type) {
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> filterList = em.createQuery("SELECT o FROM Operation  o WHERE o.fromAccount.id = : accountId AND o.operationType =: type", Operation.class)
                    .setParameter("accountId", selectedAccountId)
                    .setParameter("type", type)
                    .getResultList();
            operationList.addAll(filterList);
        });
        operationReportService.write(operationList);
        return operationList;
    }

    @Override
    public List<Operation> filterOperationByCategory(OperationType.ExpenseCategory category) {
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> filterList = em.createQuery("SELECT o FROM Operation o WHERE o.fromAccount.id = : accountId AND o.expenseCategory =: category", Operation.class)
                    .setParameter("accountId", selectedAccountId)
                    .setParameter("category", category)
                    .getResultList();
            operationList.addAll(filterList);
        });
        operationReportService.write(operationList);
        return operationList;
    }

    @Override
    public List<Operation> sortByAmount(boolean ascending) {
        List<Operation> operationList = getOperationsThisAccount();
        inSession(em -> {
            Comparator<Operation> comparator = ascending ?
                    Comparator.comparing(Operation::getAmount) :
                    Comparator.comparing(Operation::getAmount).reversed();

            Collections.sort(operationList, comparator);
        });
        operationReportService.write(operationList);
        return operationList;
    }

    @Override
    public List<Operation> findExtremeOperationOfThisAccount(boolean findMax) {
        String queryString = findMax ?
                "SELECT o FROM Operation o WHERE o.fromAccount.id = :accountId ORDER BY o.amount DESC" :
                "SELECT o FROM Operation o WHERE o.fromAccount.id = :accountId ORDER BY o.amount ASC";
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> operations = em.createQuery(queryString, Operation.class)
                    .setParameter("accountId", selectedAccountId)
                    .setMaxResults(1)
                    .getResultList();
            operationList.addAll(operations);
        });
        operationReportService.write(operationList);
        return operationList;
    }

    @Override
    public List<Operation> sortByCreatedAt(boolean ascending) {
        String queryString = ascending ?
                "SELECT o FROM Operation o WHERE o.fromAccount.id = :accountId ORDER BY o.createdAt DESC" :
                "SELECT o FROM Operation o WHERE o.fromAccount.id = :accountId ORDER BY o.createdAt ASC";
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> operations = em.createQuery(queryString, Operation.class)
                    .setParameter("accountId", selectedAccountId)
                    .getResultList();
            operationList.addAll(operations);
        });
        operationReportService.write(operationList);
        return operationList;
    }

    @Override
    public List<Operation> findOperationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String hql = "SELECT o FROM Operation o WHERE o.fromAccount.id = :accountId AND o.createdAt BETWEEN :startDate AND :endDate";
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Operation> operations = em.createQuery(hql, Operation.class)
                    .setParameter("accountId", selectedAccountId)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
            operationList.addAll(operations);
        });
        operationReportService.write(operationList);
        return operationList;
    }

    @Override
    public List<Account> getAllAccount() {
        List<Account> accountList = new ArrayList<>();
        inSession(em -> {
            List<Account> accounts = em.createQuery("SELECT a FROM Account a WHERE a.user.id = :userId", Account.class)
                    .setParameter("userId", selectedUserId)
                    .getResultList();
            accountList.addAll(accounts);
            for (Account account : accounts) {
                System.out.println("Id accounts: " + account.getId() + " , Name: " + account.getName());
            }
        });
        accountReportService.write(accountList);
        return accountList;
    }

    @Override
    public List<Account> sortAccountByBalance(boolean ascending) {
        List<Account> accountList = new ArrayList<>();
        String queryString = ascending ?
                "SELECT a FROM Account a WHERE a.user.id = :userId ORDER BY a.balance DESC" :
                "SELECT a FROM Account a WHERE a.user.id = :userId ORDER BY a.balance ASC";
        inSession(em -> {
            List<Account> accounts = em.createQuery(queryString, Account.class)
                    .setParameter("userId", selectedUserId)
                    .getResultList();
            accountList.addAll(accounts);
        });
        accountReportService.write(accountList);
        return accountList;
    }

    @Override
    public List<Operation> findExtremeOperationThisUser(boolean ascending) {
        List<Operation> operationList = new ArrayList<>();
        inSession(em -> {
            List<Account> accounts = em.createQuery("SELECT o FROM Account o WHERE o.user.id = :userId", Account.class)
                    .setParameter("userId", selectedUserId)
                    .getResultList();
            for (Account account : accounts) {
                String queryString = ascending ?
                        "SELECT o FROM Operation o WHERE o.fromAccount = :account ORDER BY o.amount DESC" :
                        "SELECT o FROM Operation o WHERE o.fromAccount = :account ORDER BY o.amount ASC";
                List<Operation> operations = em.createQuery(queryString, Operation.class)
                        .setParameter("account", account)
                        .setMaxResults(1)
                        .getResultList();
                operationList.addAll(operations);
            }
        });
        operationReportService.write(operationList);
        return operationList;
    }
}
