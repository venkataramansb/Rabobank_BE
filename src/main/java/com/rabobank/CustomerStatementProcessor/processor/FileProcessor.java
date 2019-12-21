package com.rabobank.CustomerStatementProcessor.processor;

import com.rabobank.CustomerStatementProcessor.model.Record;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileProcessor {
    List<Record> process(MultipartFile requestedFile);
}
