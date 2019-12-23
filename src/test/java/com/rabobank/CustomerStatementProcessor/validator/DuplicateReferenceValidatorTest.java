package com.rabobank.CustomerStatementProcessor.validator;

import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.ErrorTypes;
import com.rabobank.CustomerStatementProcessor.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DuplicateReferenceValidatorTest {

    @InjectMocks
    DuplicateReferenceValidator duplicateReferenceValidator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenValidTranscation_withSameReferrenceNumber_thenFailed() {
        List<ErrorTransaction> validate = duplicateReferenceValidator.validate(constructTransactionListForDuplicateReferences());
        assertEquals(2, validate.size());
        assertEquals(112806, validate.get(0).getReference());
        assertEquals(ErrorTypes.DUPLICATE_REFERENCES, validate.get(0).getErrorType());
    }

    @Test
    public void whenValidTranscation_withUniqueReferrenceNumber_thenSuccess() {
        List<ErrorTransaction> validate = duplicateReferenceValidator.validate(constructTransactionListForUniqueReferences());
        assertNotNull(validate);
    }

    private List<Transaction> constructTransactionListForDuplicateReferences() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(Transaction
                .builder()
                .reference(112806)
                .description("Clothes from Jan Bakker")
                .build());

        transactions.add(Transaction
                .builder()
                .reference(112806)
                .description("Food from Feb ross")
                .build());

        return transactions;
    }

    private List<Transaction> constructTransactionListForUniqueReferences() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(Transaction
                .builder()
                .reference(112906)
                .description("Clothes from Jan Bakker")
                .build());

        transactions.add(Transaction
                .builder()
                .reference(112806)
                .description("Food from Feb ross")
                .build());

        return transactions;
    }


}