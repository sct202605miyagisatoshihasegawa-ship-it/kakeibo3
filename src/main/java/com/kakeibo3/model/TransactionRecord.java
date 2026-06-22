package com.kakeibo3.model;

import java.time.LocalDate;

public record TransactionRecord(
        LocalDate date,
        long amount,
        CategoryType category,
        PaymentMethodType paymentMethod,
        String detail) {

    public TransactionRecord {

        if (date == null) {
            throw new IllegalArgumentException(
                    "date is null");
        }

        if (amount < 0) {
            throw new IllegalArgumentException(
                    "amount must be positive");
        }

        if (category == null) {
            throw new IllegalArgumentException(
                    "category is null");
        }
        
        if (paymentMethod == null) {
            throw new IllegalArgumentException(
                    "paymentMethod is null");
        }
        
    }

    public long signedAmount() {

        return category.isIncome()
                ? amount
                : -amount;
    }
}