package com.rabobank.CustomerStatementProcessor.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessorService {
    void processFileInformation(MultipartFile file);
}
