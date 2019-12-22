package com.rabobank.CustomerStatementProcessor.processor.impl;

import com.rabobank.CustomerStatementProcessor.model.Transaction;
import com.rabobank.CustomerStatementProcessor.processor.FileProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVFileProcessor implements FileProcessor {
    @Override
    public List<Transaction> process(MultipartFile requestedFile) {
        log.info("Inside " + CSVFileProcessor.class + "process method");
        List<Transaction> transactions = new ArrayList<>();
        try {
            String[] arrayOfRecord = new String(requestedFile.getBytes()).split("[\\r\\n]+");
            transactions = constructRecordBOFromFile(arrayOfRecord);

        } catch (Exception e) {
            log.error("process" + " -  " + "Exception " + e.getMessage());
            //  return "Error while processing the csv file " + e.getMessage();
        }
        return transactions;
    }

    private List<Transaction> constructRecordBOFromFile(String[] arrayOfRecord) {
        log.info("constructRecordBOFromFile");
        List<Transaction> transactions = new ArrayList<>();
        for (int x = 1; x < arrayOfRecord.length; x++) {
            String[] record = arrayOfRecord[x].split(",");
            Transaction rec = new Transaction();
            for (int i = 0; i < record.length; i++) {
                rec.setReference(Integer.parseInt(record[0]));
                rec.setAccountNumber((record[1]));
                rec.setDescription(record[2]);
                rec.setStartBalance(Double.parseDouble(record[3]));
                rec.setMutation(Double.parseDouble(record[4]));
                rec.setEndBalance(Double.parseDouble(record[5]));
            }
            transactions.add(rec);
        }
        return transactions;
    }
}
