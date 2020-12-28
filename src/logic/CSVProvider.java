package logic;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.util.List;

public class CSVProvider {

    public CSVProvider() { }

    public List<String[]> readCSV(String filePath) throws Exception {
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), "Windows-1251"), ';');

        return reader.readAll();
    }

    public void saveToCSV(String filePath, String[][] data) throws Exception {
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filePath), "Windows-1251"), ';');

        for (String[] dataLine: data) {
            writer.writeNext(dataLine);
        }

        writer.close();
    }
}
