package com.rabobank.CustomerStatementProcessor.validator;

import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.Transaction;

import java.util.List;

public interface DuplicateValidator {
     List<ErrorTransaction> validate(List<Transaction> transactions);
}
