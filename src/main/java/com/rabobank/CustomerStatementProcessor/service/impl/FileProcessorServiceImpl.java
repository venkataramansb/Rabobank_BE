package com.rabobank.CustomerStatementProcessor.service.impl;

import com.rabobank.CustomerStatementProcessor.model.Record;
import com.rabobank.CustomerStatementProcessor.processor.FileProcessor;
import com.rabobank.CustomerStatementProcessor.processor.impl.CSVFileProcessor;
import com.rabobank.CustomerStatementProcessor.processor.impl.XMLFileProcessor;
import com.rabobank.CustomerStatementProcessor.service.FileProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.util.*;

@Service
@Slf4j
public class FileProcessorServiceImpl implements FileProcessorService {

    private static final HashMap<String, FileProcessor> factoryProcessorMap = new HashMap<>(2);

    static {
        factoryProcessorMap.put("CSV", new CSVFileProcessor());
        factoryProcessorMap.put("XML", new XMLFileProcessor());
    }

    @Override
    public void processFileInformation(MultipartFile requestedFile) {
        String fileType = determineFileType(requestedFile);

        FileProcessor fileProcessor = factoryProcessorMap.get(fileType);
        List<Record> processedList = fileProcessor.process(requestedFile);
        log.info(processedList.toString());
        List<Record> invalidTranscationList = validateTransactions(processedList);
        getInvalidTransactions(invalidTranscationList);

    }

    private String determineFileType(MultipartFile requestedFile) {
        log.info("determineFileType");
        String fileName = requestedFile.getOriginalFilename();
        String fileType = fileName.substring(fileName.length() - 3);
        return fileType.toUpperCase();
    }

    private List<Record> validateTransactions(List<Record> transactionRecords) {
        log.info("validateTransactions");
        Map<Integer, List<Record>> transactionReferenceCountMap = new HashMap<>();
        List<Record> incorrectTransactions = new ArrayList<>();
        for (Record bo : transactionRecords) {
            int transactionReference = bo.getReference();

            if (transactionReferenceCountMap.containsKey(transactionReference)) {
                transactionReferenceCountMap.get(transactionReference).add(bo);
            } else {
                addToExistingListForDuplicateTransaction(transactionReferenceCountMap, bo, transactionReference);
            }

            addIncorrectEndValueTransactionToMap(incorrectTransactions, bo, calculateEndBalance(bo));
        }
        addDuplicateTransactionToList(transactionReferenceCountMap, incorrectTransactions);
        return incorrectTransactions;
    }


    private String getInvalidTransactions(List<Record> transactions) {
        log.info("getInvalidTransactions");
        StringBuilder sb = new StringBuilder();
        System.out.println("FileProcessorServiceImpl.getInvalidTransactions --------"+transactions.size());
        for (Record record : transactions) {
            sb.append("Transaction reference number: ").append(record.getReference()).append(" , ")
                    .append("Description is :").append(record.getDescription()).append("\n");
        }
        log.info("invalidTransactions \n" + sb.toString());
        return sb.toString();
    }

    private void addToExistingListForDuplicateTransaction(Map<Integer, List<Record>> transactionReferenceCountMap, Record record,
                                                          int transactionReference) {
        List<Record> list = new ArrayList<>();
        list.add(record);
        transactionReferenceCountMap.put(transactionReference, list);
    }

    private void addIncorrectEndValueTransactionToMap(List<Record> incorrectTransactions, Record record, float endBalance) {
        if (endBalance != record.getEndBalance()) {
            incorrectTransactions.add(record);
        }
    }

    private void addDuplicateTransactionToList(Map<Integer, List<Record>> transactionRefCount,
                                               List<Record> incorrectTransactions) {
        for (Map.Entry<Integer, List<Record>> entry : transactionRefCount.entrySet()) {
            if (entry.getValue().size() > 1) {
                incorrectTransactions.addAll(entry.getValue());
            }
        }
    }

    private float calculateEndBalance(Record bo) {
        NumberFormat formatter = requiredNumberFormat();
        float startBalance = bo.getStartBalance();
        String mutation = bo.getMutation();
        int length = mutation.length();
        if (mutation.substring(0, 1).equals("-")) {
            return deductiveMutation(formatter, startBalance, mutation, length);
        } else
            return additiveMutation(formatter, startBalance, mutation, length);
    }

    private Float additiveMutation(NumberFormat formatter, float startBalance, String mutation, int length) {
        return new Float(formatter.format((startBalance + Float.parseFloat(mutation.substring(1, length)))));
    }

    private Float deductiveMutation(NumberFormat formatter, float startBalance, String mutation, int length) {
        return new Float(formatter.format((startBalance - Float.parseFloat(mutation.substring(1, length)))));
    }


    private NumberFormat requiredNumberFormat() {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);
        formatter.setGroupingUsed(false);
        return formatter;
    }
}
