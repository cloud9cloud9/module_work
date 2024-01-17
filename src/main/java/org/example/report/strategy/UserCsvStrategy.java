package org.example.report.strategy;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.example.Entity.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UserCsvStrategy implements CsvStrategy<User>{
    @Override
    public List<User> readFromCsv(String fileName) {
        try (Reader reader = Files.newBufferedReader(Paths.get(fileName))) {
            CsvToBean<User> csvToBean = new CsvToBeanBuilder<User>(reader)
                    .withType(User.class)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToCsv(String fileName, List<User> dataList) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(fileName))) {
            StatefulBeanToCsv<User> beanToCsv = new StatefulBeanToCsvBuilder<User>(writer)
                    .withSeparator(',')
                    .build();

            beanToCsv.write(dataList);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
