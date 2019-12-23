package com.rabobank.CustomerStatementProcessor.service.impl;

import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.Transaction;
import com.rabobank.CustomerStatementProcessor.processor.FileProcessor;
import com.rabobank.CustomerStatementProcessor.processor.impl.CSVFileProcessor;
import com.rabobank.CustomerStatementProcessor.processor.impl.XMLFileProcessor;
import com.rabobank.CustomerStatementProcessor.service.FileProcessorService;
import com.rabobank.CustomerStatementProcessor.validator.DuplicateRecordValidator;
import com.rabobank.CustomerStatementProcessor.validator.EndBalanceRecordValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileProcessorServiceImpl implements FileProcessorService {

    @Autowired
    private EndBalanceRecordValidator endBalanceRecordValidator;

    @Autowired
    private DuplicateRecordValidator duplicateRecordValidator;

    private static final HashMap<String, FileProcessor> factoryProcessorMap = new HashMap<>(2);

    static {
        factoryProcessorMap.put("CSV", new CSVFileProcessor());
        factoryProcessorMap.put("XML", new XMLFileProcessor());
    }

    @Override
    public List<ErrorTransaction> processFileInformation(MultipartFile requestedFile) {
        log.info("Entering into processFileInformation" + requestedFile.getName());
        String fileType = determineFileType(requestedFile);
        FileProcessor fileProcessor = factoryProcessorMap.get(fileType);
        List<Transaction> processedList = fileProcessor.process(requestedFile);
        List<ErrorTransaction> invalidTranscationList = validateTransactions(processedList);
        log.info("Exit into processFileInformation " + this.getClass().getName());
        return invalidTranscationList;
    }

    private String determineFileType(MultipartFile requestedFile) {
        log.info("Entering into determineFileType " + this.getClass().getName());
        String fileName = requestedFile.getOriginalFilename();
        String fileType = fileName.substring(fileName.length() - 3);
        return fileType.toUpperCase();
    }

    private List<ErrorTransaction> validateTransactions(List<Transaction> transactions) {
        log.info("Entering into validateTransactions " + this.getClass().getName());
        List<ErrorTransaction> errorTransactions = transactions.stream()
                .map(x -> endBalanceRecordValidator.validate(x))
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
        errorTransactions.addAll(duplicateRecordValidator.validate(transactions));
        log.info("Exit into validateTransactions " + this.getClass().getName());

        return errorTransactions;
    }
}
