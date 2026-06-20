package com.kakeibo3.model;

public record CategorySummaryRecord(
        CategoryType category,
        long totalAmount) {

    public String toDisplayText() {

        return category.getLabel()
                + " ¥"
                + String.format("%,d",
                        Math.abs(totalAmount));
    }
}