package org.example.report.strategy;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.example.util.PropertiesUtil;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DoubleCsvStrategy implements CsvStrategy<Double> {
    private final String filePath;

    public DoubleCsvStrategy() {
        this.filePath = PropertiesUtil.get("doubleCsvPath");
    }

    @Override
    public List<Double> readFromCsv() {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> lines = csvReader.readAll();

            return lines.stream()
                    .map(array -> Double.valueOf(array[0]))
                    .toList();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToCsv(List<Double> dataList) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(filePath))) {
            CSVWriter csvWriter = new CSVWriter(writer);
            dataList.forEach(value -> csvWriter.writeNext(new String[]{value.toString()}));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
