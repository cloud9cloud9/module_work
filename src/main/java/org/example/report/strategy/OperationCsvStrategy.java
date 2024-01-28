package org.example.report.strategy;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.example.Entity.Operation;
import org.example.util.PropertiesUtil;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class OperationCsvStrategy implements CsvStrategy<Operation>{
    private final String filePath;
    public OperationCsvStrategy() {
        this.filePath = PropertiesUtil.get("operationCsvPath");
    }
    @Override
    public List<Operation> readFromCsv() {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            CsvToBean<Operation> csvToBean = new CsvToBeanBuilder<Operation>(reader)
                    .withType(Operation.class)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToCsv(List<Operation> dataList) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(filePath))) {
            StatefulBeanToCsv<Operation> beanToCsv = new StatefulBeanToCsvBuilder<Operation>(writer)
                    .withSeparator(',')
                    .build();

            beanToCsv.write(dataList);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
