package org.example.report;

import org.example.report.strategy.CsvStrategy;
import java.util.List;

public class ReportService<T> {

    private CsvStrategy<T> csvStrategy;

    private ReportService(CsvStrategy<T> csvStrategy) {
        this.csvStrategy = csvStrategy;
    }

    public static <T> ReportService<T> createReportService(CsvStrategy<T> csvStrategy) {
        return new ReportService<>(csvStrategy);
    }

    public void write(List<T> dataList) {
        csvStrategy.writeToCsv(dataList);
    }

    public List<T> read() {
        return csvStrategy.readFromCsv();
    }
}