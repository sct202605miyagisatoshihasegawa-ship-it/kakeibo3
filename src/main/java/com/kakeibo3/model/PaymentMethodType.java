package com.kakeibo3.model;

public enum PaymentMethodType {

    BANK_A(
            "銀行口座A"),

    BANK_B(
            "銀行口座B"),

    CREDIT_CARD(
            "クレジットカード"),

    CASH(
            "現金");

    private final String label;

    PaymentMethodType(
            String label) {

        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * DB読込用
     * Enum名(BANK_A)でも
     * 表示名(銀行口座A)でも読める
     */
    public static PaymentMethodType fromDbValue(
            String value) {

        if (value == null) {
            return null;
        }

        // まず Enum名を試す
        try {
            return PaymentMethodType.valueOf(
                    value);
        } catch (IllegalArgumentException e) {
            // ラベル照合へ
        }

        // 表示名照合
        for (PaymentMethodType type
                : values()) {

            if (type.label.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException(
                "Unknown payment method: "
                        + value);
    }

    @Override
    public String toString() {
        return label;
    }
}