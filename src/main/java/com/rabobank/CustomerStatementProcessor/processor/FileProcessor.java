package com.rabobank.CustomerStatementProcessor.processor;

import com.rabobank.CustomerStatementProcessor.model.Transaction;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileProcessor {
    List<Transaction> process(MultipartFile requestedFile);
}
