package com.kakeibo3.service;

import java.util.List;

import com.kakeibo3.model.TransactionRecord;

public final class SummaryCalculator {

    private SummaryCalculator() {
    }

    public static long calculateBalance(
            List<TransactionRecord> records) {

        return records.stream()
                .mapToLong(
                        TransactionRecord::signedAmount)
                .sum();
    }
}