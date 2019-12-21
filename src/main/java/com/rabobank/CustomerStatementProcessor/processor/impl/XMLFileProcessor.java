package com.rabobank.CustomerStatementProcessor.processor.impl;

import com.rabobank.CustomerStatementProcessor.model.Record;
import com.rabobank.CustomerStatementProcessor.model.Records;
import com.rabobank.CustomerStatementProcessor.processor.FileProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.List;

@Slf4j
public class XMLFileProcessor implements FileProcessor {
    @Override
    public List<Record> process(MultipartFile requestedFile) {
        log.info("Inside "+XMLFileProcessor.class + "process method");
        Records records = constructRecordBOFromFile(requestedFile);
        return records.getRecord();
    }

    private Records constructRecordBOFromFile(MultipartFile requestedFile) {
        log.info("constructRecordBOFromFile");
        Records records = null;
        try {
            StringReader sr = new StringReader(new String(requestedFile.getBytes()));
            JAXBContext jaxbContext = JAXBContext.newInstance(Records.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            records = (Records) unmarshaller.unmarshal(sr);
        } catch (Exception e) {
           log.info("constructRecordBOFromFile" + " -  " + "Exception " + e.getMessage());
        }
        return records;
    }
}
