package org.example.report.strategy;

import java.util.List;

public interface CsvStrategy<T> {
    List<T> readFromCsv();
    void writeToCsv(List<T> dataList);
}

