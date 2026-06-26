package com.kakeibo3.controller.cell;

import com.kakeibo3.model.TransactionProperty;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class DetailTableCell
		extends TableCell<TransactionProperty, String> {

	private final TextField textField =
			new TextField();

	public DetailTableCell() {

		textField.addEventFilter(
				KeyEvent.KEY_PRESSED,
				event -> {

					if (event.getCode() == KeyCode.TAB) {

						event.consume();
					}
				});

		textField.setOnAction(
				event -> commitCurrentValue());
	}

	private void commitCurrentValue() {

		commitEdit(
				textField.getText());
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
						: getItem());

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
						: getItem());

		setContentDisplay(
				ContentDisplay.TEXT_ONLY);
	}

	@Override
	protected void updateItem(
			String item,
			boolean empty) {

		super.updateItem(
				item,
				empty);

		if (empty) {

			setText(null);
			setGraphic(null);

		} else if (isEditing()) {

			textField.setText(
					item == null
							? ""
							: item);

			setGraphic(textField);

			setContentDisplay(
					ContentDisplay.GRAPHIC_ONLY);

		} else {

			setText(
					item == null
							? ""
							: item);

			setContentDisplay(
					ContentDisplay.TEXT_ONLY);
		}
	}
}