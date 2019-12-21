package com.rabobank.CustomerStatementProcessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementProcessorResponse {

    private String decription;
    private int transactionReference;
    private int status;
}
