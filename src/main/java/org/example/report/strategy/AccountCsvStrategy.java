package org.example.report.strategy;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.example.Entity.Account;
import org.example.util.PropertiesUtil;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AccountCsvStrategy implements CsvStrategy<Account> {
    private final String filePath;
    public AccountCsvStrategy() {
        this.filePath = PropertiesUtil.get("accountCsvPath");
    }
    @Override
    public List<Account> readFromCsv() {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            CsvToBean<Account> csvToBean = new CsvToBeanBuilder<Account>(reader)
                    .withType(Account.class)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToCsv(List<Account> dataList) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(filePath))) {
            StatefulBeanToCsv<Account> beanToCsv = new StatefulBeanToCsvBuilder<Account>(writer)
                    .withSeparator(',')
                    .build();

            beanToCsv.write(dataList);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
