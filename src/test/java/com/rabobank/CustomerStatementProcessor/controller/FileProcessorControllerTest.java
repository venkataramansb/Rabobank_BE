package com.rabobank.CustomerStatementProcessor.controller;

import com.rabobank.CustomerStatementProcessor.constants.ProcessorConstants;
import com.rabobank.CustomerStatementProcessor.helper.ControllerHelper;
import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.ErrorTypes;
import com.rabobank.CustomerStatementProcessor.model.StatementProcessorResponse;
import com.rabobank.CustomerStatementProcessor.service.FileProcessorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FileProcessorControllerTest {
    @InjectMocks
    private FileProcessorController fileProcessorController;

    @Mock
    FileProcessorService service;

    @Mock
    ControllerHelper controllerHelper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenCSVFilePassed_processStatement_returnSuccess() {
        MockMultipartFile records = MultiPartFileInput.generateCSVMultiPartFile(CsvIO.getCSVInput());
        when(controllerHelper.validateFileFormat(records)).thenReturn(true);
        when(service.processFileInformation(records)).thenReturn(constructErrorTranscationList());

        ResponseEntity<StatementProcessorResponse> statementProcessorResponseResponseEntity = fileProcessorController.processFile(records);

        assertEquals(200, statementProcessorResponseResponseEntity.getBody().getStatus());
        assertEquals(2, statementProcessorResponseResponseEntity.getBody().getErrorTransactions().size());

    }

    @Test
    public void whenXMLFilePassed_processStatement_returnSuccess() throws IOException {
       InputStream stream = fileProcessorController.getClass().getClassLoader().getResourceAsStream("records.xml");
        MockMultipartFile records = new MockMultipartFile("file", "records.xml", ProcessorConstants.FILE_TYPE_XML, stream);

        when(controllerHelper.validateFileFormat(records)).thenReturn(true);
        when(service.processFileInformation(records)).thenReturn(constructErrorTranscationList());

        ResponseEntity<StatementProcessorResponse> statementProcessorResponseResponseEntity = fileProcessorController.processFile(records);

        assertEquals(200, statementProcessorResponseResponseEntity.getBody().getStatus());
        assertEquals(2, statementProcessorResponseResponseEntity.getBody().getErrorTransactions().size());

    }


    @Test
    public void whenHTMLFilePassed_processStatement_returnSuccess() {
        when(controllerHelper.validateFileFormat(any())).thenReturn(false);

        ResponseEntity<StatementProcessorResponse> statementProcessorResponseResponseEntity = fileProcessorController.processFile(any());

        assertEquals(601, statementProcessorResponseResponseEntity.getBody().getStatus());
        assertEquals(ProcessorConstants.UNSUPORTED_FILE_FORMAT_MSG, statementProcessorResponseResponseEntity.getBody().getDecription());
    }

    private List<ErrorTransaction> constructErrorTranscationList() {
        ErrorTransaction transaction = ErrorTransaction.builder().description("Clothes for Willem Dekker")
                .reference(112806)
                .errorType(ErrorTypes.DUPLICATE_REFERENCES)
                .build();

        ErrorTransaction sameReferneceTransaction = ErrorTransaction.builder().description("Clothes for Venkat S B")
                .reference(112806)
                .errorType(ErrorTypes.DUPLICATE_REFERENCES)
                .build();

        List<ErrorTransaction> errorTransactions = new ArrayList<>();
        errorTransactions.add(transaction);
        errorTransactions.add(sameReferneceTransaction);

        return errorTransactions;
    }

}

class MultiPartFileInput {

    public static MockMultipartFile generateCSVMultiPartFile(String inputString) {
        MockMultipartFile records = new MockMultipartFile("file", "records.csv", "text/plain",
                inputString.getBytes(StandardCharsets.UTF_8));
        return records;
    }
}

class CsvIO {

    public static String getCSVInput() {
        String inputString = "Reference,AccountNumber,Description,Start Balance,Mutation,End Balance\n"
                + "194261,NL91RABO0315273637,Clothes from Jan Bakker,21.6,-41.83,-20.23\n"
                + "112806,NL27SNSB0917829871,Clothes for Willem Dekker,91.23,+15.57,106.8\n"
                + "183049,NL69ABNA0433647324,Clothes for Jan King,86.66,+44.5,131.16\n"
                + "183356,NL74ABNA0248990274,Subscription for Peter de Vries,92.98,-46.65,46.33\n"
                + "112806,NL69ABNA0433647324,Clothes for Richard de Vries,90.83,-10.91,79.92\n"
                + "112806,NL93ABNA0585619023,Tickets from Richard Bakker,102.12,+45.87,147.99\n"
                + "139524,NL43AEGO0773393871,Flowers from Jan Bakker,99.44,+41.23,140.67\n"
                + "179430,NL93ABNA0585619023,Clothes for Vincent Bakker,23.96,-27.43,-3.47\n"
                + "141223,NL93ABNA0585619023,Clothes from Erik Bakker,94.25,+41.6,135.85\n"
                + "195446,NL74ABNA0248990274,Flowers for Willem Dekker,26.32,+48.98,75.3";
        return inputString;
    }
}
