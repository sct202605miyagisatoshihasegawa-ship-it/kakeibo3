package com.kakeibo3.controller.cell;

import com.kakeibo3.model.TransactionProperty;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

public class AmountTableCell
		extends TableCell<TransactionProperty, Long> {

	private final TextField textField = new TextField();

	public AmountTableCell() {

		textField.textProperty()
				.addListener((obs,
						oldValue,
						newValue) -> {

					if (!newValue.matches("\\d*")) {

						textField.setText(
								newValue.replaceAll(
										"[^\\d]",
										""));
					}
				});

		textField.setOnAction(
				event -> commitCurrentValue());

		textField.focusedProperty()
				.addListener((obs,
						oldValue,
						newValue) -> {

					if (!newValue) {

						commitCurrentValue();
					}
				});
	}

	private void commitCurrentValue() {

		String text = textField.getText();

		if (text == null ||
				text.isBlank()) {

			cancelEdit();
			return;
		}

		commitEdit(
				Long.parseLong(text));
	}

	@Override
	public void startEdit() {

		if (!isEditable()
				|| !getTableView().isEditable()
				|| !getTableColumn().isEditable()) {

			return;
		}

		super.startEdit();

		textField.setText(
				getItem() == null
						? ""
						: getItem().toString());

		setGraphic(textField);

		setContentDisplay(
				ContentDisplay.GRAPHIC_ONLY);

		textField.requestFocus();
		textField.selectAll();
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
			Long item,
			boolean empty) {

		super.updateItem(
				item,
				empty);

		if (empty || item == null) {

			setText(null);
			setGraphic(null);

		} else if (isEditing()) {

			textField.setText(
					item.toString());

			setGraphic(textField);

			setContentDisplay(
					ContentDisplay.GRAPHIC_ONLY);

		} else {

			setText(item.toString());

			setContentDisplay(
					ContentDisplay.TEXT_ONLY);
		}
	}
}