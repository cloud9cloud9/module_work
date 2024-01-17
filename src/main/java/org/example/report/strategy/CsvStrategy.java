package org.example.report.strategy;

import java.util.List;

public interface CsvStrategy<T> {
    List<T> readFromCsv(String fileName);
    void writeToCsv(String fileName, List<T> dataList);
}

