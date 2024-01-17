package org.example.report.strategy;

import org.example.Entity.Account;

import java.util.List;

public class AccountCsvStrategy implements CsvStrategy<Account> {
    @Override
    public List<Account> readFromCsv(String fileName) {
        return null;
    }

    @Override
    public void writeToCsv(String fileName, List<Account> dataList) {

    }
}
