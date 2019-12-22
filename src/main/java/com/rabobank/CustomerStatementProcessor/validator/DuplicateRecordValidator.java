package com.rabobank.CustomerStatementProcessor.validator;

import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DuplicateRecordValidator {

    @Autowired
    private List<DuplicateValidator> validators;

    public List<ErrorTransaction> validate(List<Transaction> transactions) {
        return this.validators.stream()
                .map(x -> x.validate(transactions))
                .flatMap(x -> x.stream())
                .filter(x -> x != null)
                .collect(Collectors.toList());
    }
}
