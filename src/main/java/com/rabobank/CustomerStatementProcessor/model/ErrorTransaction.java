package com.rabobank.CustomerStatementProcessor.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ErrorTransaction {
  private int reference;
  private String description;
  private ErrorTypes errorType;
 }
