package com.rabobank.CustomerStatementProcessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementProcessorResponse {

    private int status;
    private String decription;
    private List<ErrorTransaction> errorTransactions;
}
