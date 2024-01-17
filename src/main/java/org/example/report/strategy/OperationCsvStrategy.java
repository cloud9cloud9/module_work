package org.example.report.strategy;

import org.example.Entity.Operation;

import java.util.List;

public class OperationCsvStrategy implements CsvStrategy<Operation>{
    @Override
    public List<Operation> readFromCsv(String fileName) {
        return null;
    }

    @Override
    public void writeToCsv(String fileName, List<Operation> dataList) {

    }
}
