package com.rabobank.CustomerStatementProcessor.processor.impl;

import com.rabobank.CustomerStatementProcessor.model.Transaction;
import com.rabobank.CustomerStatementProcessor.model.Transactions;
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
    public List<Transaction> process(MultipartFile requestedFile) {
        log.info("Inside "+XMLFileProcessor.class + "process method");
        Transactions transactions = constructRecordBOFromFile(requestedFile);
        return transactions.getTransaction();
    }

    private Transactions constructRecordBOFromFile(MultipartFile requestedFile) {
        log.info("constructRecordBOFromFile");
        Transactions transactions = null;
        try {
            StringReader sr = new StringReader(new String(requestedFile.getBytes()));
            JAXBContext jaxbContext = JAXBContext.newInstance(Transactions.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            transactions = (Transactions) unmarshaller.unmarshal(sr);
        } catch (Exception e) {
           log.info("constructRecordBOFromFile" + " -  " + "Exception " + e.getMessage());
        }
        return transactions;
    }
}
