package com.rabobank.CustomerStatementProcessor.validator;

import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.Transaction;

public interface EndBalanceValidator {
    public ErrorTransaction validate(Transaction transaction);
}
