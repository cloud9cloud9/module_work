package org.example.report;

import org.example.report.strategy.CsvStrategy;
import java.util.List;

public class ReportService {

   private CsvStrategy csvStrategy;

    private ReportService(CsvStrategy csvStrategy) {
        this.csvStrategy = csvStrategy;
    }
    public static ReportService createReportService(CsvStrategy csvStrategy){
        return new ReportService(csvStrategy);
    }

    public <T> void write(List<T> dataList){
        csvStrategy.writeToCsv(dataList);
    }
    public<T> List<T> read (){
        return csvStrategy.readFromCsv();
    }
}
