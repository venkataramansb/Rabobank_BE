package com.rabobank.CustomerStatementProcessor.service;

import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileProcessorService {
    List<ErrorTransaction> processFileInformation(MultipartFile file);
}
