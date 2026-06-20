package com.kakeibo3.model;

public enum CategoryType {

    SALARY(
            "給与",
            true),

    PART_TIME_OTHER(
            "パート収入など",
            true),

    HOME_LOAN(
            "住宅ローン",
            false),

    CREDIT_CARD_PAYMENT(
            "先月分クレジットカード引落",
            false),

    FOOD(
            "食費",
            false),

    UTILITIES(
            "水道光熱費",
            false),

    COMMUNICATION(
            "通信費",
            false),

    EDUCATION(
            "教育費",
            false),

    LEISURE(
            "娯楽・レジャー",
            false),

    SOCIAL(
            "交際費",
            false),

    OTHER(
            "その他",
            false);

    private final String label;

    private final boolean income;

    CategoryType(
            String label,
            boolean income) {

        this.label = label;
        this.income = income;
    }

    public String getLabel() {
        return label;
    }

    public boolean isIncome() {
        return income;
    }
}