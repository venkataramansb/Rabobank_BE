package com.rabobank.CustomerStatementProcessor.validator;

import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.ErrorTypes;
import com.rabobank.CustomerStatementProcessor.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class EndEndBalanceReferenceValidatorTest {

    @InjectMocks
    EndEndBalanceReferenceValidator endEndBalanceReferenceValidator;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenTransactionCalled_vaidatedClosingBalance_thenSuccess() {
        ErrorTransaction validate = endEndBalanceReferenceValidator.validate(constructTransaction());

        assertNull(validate);
    }

    @Test
    public void whenTransactionCalled_invaidaClosingBalance_thenFailed() {
        ErrorTransaction validate = endEndBalanceReferenceValidator.validate(constructInvalidTransaction());

        assertEquals(ErrorTypes.BALANCE_MISMATCH,validate.getErrorType());
    }


    private Transaction constructTransaction() {
        return Transaction.builder()
                .reference(12321)
                .endBalance(21.12)
                .mutation(10.12)
                .startBalance(11)
                .description("Shopping")
                .build();
    }

    private Transaction constructInvalidTransaction() {
        return Transaction.builder()
                .reference(12321)
                .endBalance(891.12)
                .mutation(10.12)
                .startBalance(11)
                .description("Shopping")
                .build();
    }

}