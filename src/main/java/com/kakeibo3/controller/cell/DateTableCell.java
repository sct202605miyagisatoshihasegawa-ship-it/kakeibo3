package com.kakeibo3.controller.cell;

import java.time.LocalDate;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import com.kakeibo3.model.TransactionProperty;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

public class DateTableCell
		extends TableCell<TransactionProperty, LocalDate> {

	private final DatePicker datePicker =
			new DatePicker();

	private final IntSupplier yearSupplier;

	private final Supplier<Integer> monthSupplier;

	public DateTableCell(
			IntSupplier yearSupplier,
			Supplier<Integer> monthSupplier) {

		this.yearSupplier = yearSupplier;
		this.monthSupplier = monthSupplier;

		datePicker.setEditable(false);

		datePicker.setDayCellFactory(
				picker -> new DateCell() {

					@Override
					public void updateItem(
							LocalDate item,
							boolean empty) {

						super.updateItem(
								item,
								empty);

						if (empty || item == null) {
							return;
						}

						Integer month =
								monthSupplier.get();

						if (month == null) {
							return;
						}

						int year =
								yearSupplier.getAsInt();

						if (item.getYear() != year
								||
								item.getMonthValue() != month) {

							setDisable(true);
						}
					}
				});

		datePicker.setOnAction(event -> {

			commitEdit(
					datePicker.getValue());
		});

		datePicker.focusedProperty()
				.addListener((obs,
						oldValue,
						newValue) -> {

					if (!newValue) {

						commitEdit(
								datePicker.getValue());
					}
				});
	}

	@Override
	public void startEdit() {

		if (!isEditable()
				|| !getTableView().isEditable()
				|| !getTableColumn().isEditable()) {

			return;
		}

		super.startEdit();

		datePicker.setValue(
				getItem());

		setGraphic(datePicker);

		setContentDisplay(
				ContentDisplay.GRAPHIC_ONLY);
	}

	@Override
	public void cancelEdit() {

		super.cancelEdit();

		setText(
				getItem() == null
						? ""
						: getItem().toString());

		setContentDisplay(
				ContentDisplay.TEXT_ONLY);
	}

	@Override
	protected void updateItem(
			LocalDate item,
			boolean empty) {

		super.updateItem(
				item,
				empty);

		if (empty || item == null) {

			setText(null);
			setGraphic(null);

		} else if (isEditing()) {

			datePicker.setValue(item);

			setGraphic(datePicker);

			setContentDisplay(
					ContentDisplay.GRAPHIC_ONLY);

		} else {

			setText(item.toString());

			setContentDisplay(
					ContentDisplay.TEXT_ONLY);
		}
	}
}
