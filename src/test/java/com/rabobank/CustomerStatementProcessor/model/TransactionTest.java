package com.rabobank.CustomerStatementProcessor.model;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransactionTest {

    private Transaction transaction;

    @Before
    public void setup() {
        transaction = new Transaction();
    }

    @Test
    public void whenExactClosingBalancePassed_hasValidClosingAmount_returnsTrue() {
        transaction.setEndBalance(-20.23);
        transaction.setMutation(-41.83);
        transaction.setStartBalance(21.6);

        assertTrue(transaction.hasValidClosingAmount());

    }

    @Test
    public void whenInExactClosingBalancePassed_hasValidClosingAmount_returnsFalse() {
        transaction.setEndBalance(-20.23);
        transaction.setMutation(41.83);
        transaction.setStartBalance(21.6);

        assertFalse(transaction.hasValidClosingAmount());

    }
}