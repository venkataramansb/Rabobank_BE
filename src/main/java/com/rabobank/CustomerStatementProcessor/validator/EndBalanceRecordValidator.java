package com.rabobank.CustomerStatementProcessor.validator;

import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EndBalanceRecordValidator {
    @Autowired
    private List<EndEndBalanceReferenceValidator> balanceReferenceValidators;


    public List<ErrorTransaction> validate(Transaction transaction) {
        return balanceReferenceValidators.stream()
                .map(x -> x.validate(transaction))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
