package com.kakeibo3.model;

import java.time.LocalDate;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TransactionProperty {

	private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

	private final LongProperty amount = new SimpleLongProperty();

	private final ObjectProperty<CategoryType> category = new SimpleObjectProperty<>();

	private final ObjectProperty<PaymentMethodType> paymentMethod = new SimpleObjectProperty<>();

	private final StringProperty detail = new SimpleStringProperty();

	public TransactionProperty() {
	}

	public TransactionProperty(TransactionRecord record) {

		setDate(record.date());
		setAmount(record.amount());
		setCategory(record.category());
		setPaymentMethod(record.paymentMethod());
		setDetail(record.detail());
	}

	// -------------------------
	// date
	// -------------------------

	public LocalDate getDate() {
		return date.get();
	}

	public void setDate(LocalDate value) {
		date.set(value);
	}

	public ObjectProperty<LocalDate> dateProperty() {
		return date;
	}

	// -------------------------
	// amount
	// -------------------------

	public long getAmount() {
		return amount.get();
	}

	public void setAmount(long value) {
		amount.set(value);
	}

	public LongProperty amountProperty() {
		return amount;
	}

	// -------------------------
	// category
	// -------------------------

	public CategoryType getCategory() {
		return category.get();
	}

	public void setCategory(CategoryType value) {
		category.set(value);
	}

	public ObjectProperty<CategoryType> categoryProperty() {
		return category;
	}

	// -------------------------
	// paymentMethod
	// -------------------------

	public PaymentMethodType getPaymentMethod() {
		return paymentMethod.get();
	}

	public void setPaymentMethod(
	        PaymentMethodType value) {
		paymentMethod.set(value);
	}

	public ObjectProperty<PaymentMethodType>
	paymentMethodProperty() {
		return paymentMethod;
	}

	// -------------------------
	// detail
	// -------------------------

	public String getDetail() {
		return detail.get();
	}

	public void setDetail(String value) {
		detail.set(value);
	}

	public StringProperty detailProperty() {
		return detail;
	}

	/**
	 * UI → DB変換
	 */
	public TransactionRecord toRecord() {

		return new TransactionRecord(
				getDate(),
				getAmount(),
				getCategory(),
				getPaymentMethod(),
				getDetail());
	}
}