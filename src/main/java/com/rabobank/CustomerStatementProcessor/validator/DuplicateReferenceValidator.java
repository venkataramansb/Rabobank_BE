package com.rabobank.CustomerStatementProcessor.validator;

import com.google.common.collect.Lists;
import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.ErrorTypes;
import com.rabobank.CustomerStatementProcessor.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DuplicateReferenceValidator implements DuplicateValidator {

    @Override
    public List<ErrorTransaction> validate(List<Transaction> transactions) {
        log.info("Entering into Validate " + DuplicateReferenceValidator.class);
        Map<Integer, List<Transaction>> transactionMap = new HashMap<>();
        List<Integer> duplicates = new ArrayList<>();
        transactions.forEach(
                transaction -> {
                    final int reference = transaction.getReference();
                    List<Transaction> appendedList = Lists.newArrayList(transaction);
                    if (transactionMap.containsKey(reference)) {
                        appendedList.addAll(transactionMap.get(reference));
                        duplicates.add(reference);
                    }
                    transactionMap.put(reference, appendedList);
                });
        if (duplicates.isEmpty()) {
            return new ArrayList<>();
        }
        log.info("Exit from Validate " + DuplicateReferenceValidator.class);
        return duplicates.stream()
                .map(x -> transactionMap.get(x))
                .flatMap(x -> x.stream())
                .map(x -> ErrorTransaction.builder().reference(x.getReference())
                        .description(x.getDescription())
                        .errorType(ErrorTypes.DUPLICATE_REFERENCES)
                        .build())
                .distinct()
                .collect(Collectors.toList());
    }
}
