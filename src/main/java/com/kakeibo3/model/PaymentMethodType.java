package com.kakeibo3.model;

public enum PaymentMethodType {

    BANK_A(
            "銀行口座A"),

    BANK_B(
            "銀行口座B"),

    CREDIT_CARD(
            "クレジットカード"),

    CASH(
            "手元現金");

    private final String label;

    PaymentMethodType(
            String label) {

        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
