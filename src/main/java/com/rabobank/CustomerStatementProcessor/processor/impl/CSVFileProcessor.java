package com.rabobank.CustomerStatementProcessor.processor.impl;

import com.rabobank.CustomerStatementProcessor.model.Record;
import com.rabobank.CustomerStatementProcessor.processor.FileProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVFileProcessor implements FileProcessor {
    @Override
    public List<Record> process(MultipartFile requestedFile) {
        log.info("Inside " + CSVFileProcessor.class + "process method");
        List<Record> transactions = new ArrayList<>();
        try {
            String[] arrayOfRecord = new String(requestedFile.getBytes()).split("[\\r\\n]+");
            transactions = constructRecordBOFromFile(arrayOfRecord);

        } catch (Exception e) {
            log.error("process" + " -  " + "Exception " + e.getMessage());
            //  return "Error while processing the csv file " + e.getMessage();
        }
        return transactions;
    }

    private List<Record> constructRecordBOFromFile(String[] arrayOfRecord) {
        log.info("constructRecordBOFromFile");
        List<Record> transactions = new ArrayList<>();
        for (int x = 1; x < arrayOfRecord.length; x++) {
            String[] record = arrayOfRecord[x].split(",");
            Record rec = new Record();
            for (int i = 0; i < record.length; i++) {
                rec.setReference(Integer.parseInt(record[0]));
                rec.setAccountNumber((record[1]));
                rec.setDescription(record[2]);
                rec.setStartBalance(Float.parseFloat(record[3]));
                rec.setMutation(record[4]);
                rec.setEndBalance(Float.parseFloat(record[5]));
            }
            transactions.add(rec);
        }
        return transactions;
    }
}
