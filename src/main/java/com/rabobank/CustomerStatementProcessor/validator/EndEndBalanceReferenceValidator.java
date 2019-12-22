package com.rabobank.CustomerStatementProcessor.validator;

import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.ErrorTypes;
import com.rabobank.CustomerStatementProcessor.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EndEndBalanceReferenceValidator implements EndBalanceValidator {
    @Override
    public ErrorTransaction validate(Transaction transaction) {
        if (transaction.hasValidClosingAmount()) {
            return null;
        }
        return ErrorTransaction.builder().reference(transaction.getReference())
                .description(transaction.getDescription())
                .errorType(ErrorTypes.BALANCE_MISMATCH)
                .build();
    }
}
