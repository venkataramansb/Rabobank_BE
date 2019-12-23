
package com.rabobank.CustomerStatementProcessor.service.impl;

import com.rabobank.CustomerStatementProcessor.constants.ProcessorConstants;
import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.ErrorTypes;
import com.rabobank.CustomerStatementProcessor.model.Transaction;
import com.rabobank.CustomerStatementProcessor.processor.FileProcessor;
import com.rabobank.CustomerStatementProcessor.validator.DuplicateRecordValidator;
import com.rabobank.CustomerStatementProcessor.validator.EndBalanceRecordValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class FileProcessorServiceImplTest {

    @InjectMocks
    FileProcessorServiceImpl fileProcessorService;

    @Mock
    FileProcessor fileProcessor;

    @Mock
    EndBalanceRecordValidator endBalanceRecordValidator;

    @Mock
    DuplicateRecordValidator duplicateRecordValidator;

    private InputStream stream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processCSVFile_callServiceImpl_loadCSVProcessor() throws IOException {
        stream = fileProcessorService.getClass().getClassLoader().getResourceAsStream("records.csv");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "records.csv", ProcessorConstants.FILE_TYPE_CSV, stream);

        when(fileProcessor.process(mockMultipartFile)).thenReturn(constructTranscationList());
        when(endBalanceRecordValidator.validate(constructTranscationList().get(0))).thenReturn(constructErrorTransactionListForBalanaceMismatch());
        when(duplicateRecordValidator.validate(constructTranscationList())).thenReturn(constructErrorTransactionListForDuplicateReferences());

        List<ErrorTransaction> errorTransactions = fileProcessorService.processFileInformation(mockMultipartFile);
        assertNotNull(errorTransactions);
    }

    @Test
    public void processXMLFile_callServiceImpl_loadCSVProcessor() throws IOException {
        stream = fileProcessorService.getClass().getClassLoader().getResourceAsStream("records.xml");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "records.xml", ProcessorConstants.FILE_TYPE_XML, stream);

        when(fileProcessor.process(mockMultipartFile)).thenReturn(constructTranscationList());
        when(endBalanceRecordValidator.validate(constructTranscationList().get(0))).thenReturn(constructErrorTransactionListForBalanaceMismatch());
        when(duplicateRecordValidator.validate(constructTranscationList())).thenReturn(constructErrorTransactionListForDuplicateReferences());

        List<ErrorTransaction> errorTransactions = fileProcessorService.processFileInformation(mockMultipartFile);
        assertNotNull(errorTransactions);
    }

    private List<ErrorTransaction> constructErrorTransactionListForDuplicateReferences() {
        List<ErrorTransaction> errorTransactions = new ArrayList<>();
        errorTransactions.add(ErrorTransaction
                .builder()
                .errorType(ErrorTypes.DUPLICATE_REFERENCES)
                .reference(112806)
                .description("Clothes from Jan Bakker")
                .build());

        errorTransactions.add(ErrorTransaction
                .builder()
                .errorType(ErrorTypes.DUPLICATE_REFERENCES)
                .reference(112806)
                .description("Food from Feb ross")
                .build());

        return errorTransactions;
    }

    private List<ErrorTransaction> constructErrorTransactionListForBalanaceMismatch() {
        List<ErrorTransaction> errorTransactions = new ArrayList<>();
        errorTransactions.add(ErrorTransaction
                .builder()
                .errorType(ErrorTypes.BALANCE_MISMATCH)
                .reference(112806)
                .description("Clothes from Jan Bakker")
                .build());

        errorTransactions.add(ErrorTransaction
                .builder()
                .errorType(ErrorTypes.BALANCE_MISMATCH)
                .reference(112906)
                .description("Food from Feb ross")
                .build());

        return errorTransactions;
    }

    private List<Transaction> constructTranscationList() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(
                new Transaction(
                        194261, "NL91RABO0315273637", -41.83, 21.6, "Clothes from Karthcik Selva", -20.23));
        transactions.add(
                new Transaction(
                        112806, "NL27SNSB0917829871", 15.57, 91.23, "Clothes from Jan Bakker", 506.8));
        transactions.add(
                new Transaction(
                        112806, "NL27SNSB0917829871", 15.8, 1.2, "Food from Feb ross", 17.2));

        return transactions;
    }
}
