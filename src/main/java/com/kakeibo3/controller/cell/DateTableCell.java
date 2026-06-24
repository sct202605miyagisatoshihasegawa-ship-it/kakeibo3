package com.kakeibo3.controller.cell;

import java.time.LocalDate;

import com.kakeibo3.model.TransactionProperty;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

public class DateTableCell
		extends TableCell<TransactionProperty, LocalDate> {

	private final DatePicker datePicker =
			new DatePicker();

	public DateTableCell() {

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
