package com.kakeibo3.controller.cell;

import com.kakeibo3.model.CategoryType;
import com.kakeibo3.model.TransactionProperty;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CategoryTableCell
		extends TableCell<TransactionProperty, CategoryType> {

	private final ComboBox<CategoryType> comboBox =
			new ComboBox<>(
					FXCollections.observableArrayList(
							CategoryType.values()));

	public CategoryTableCell() {

		comboBox.addEventFilter(
				KeyEvent.KEY_PRESSED,
				event -> {

					if (event.getCode() == KeyCode.TAB) {

						event.consume();
					}
				});

		comboBox.setOnAction(event -> {

			commitEdit(
					comboBox.getValue());
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

		comboBox.setValue(
				getItem());

		setGraphic(comboBox);

		setContentDisplay(
				ContentDisplay.GRAPHIC_ONLY);

		comboBox.requestFocus();

		comboBox.show();
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
			CategoryType item,
			boolean empty) {

		super.updateItem(
				item,
				empty);

		if (empty) {

			setText(null);
			setGraphic(null);

		} else if (isEditing()) {

			comboBox.setValue(item);

			setGraphic(comboBox);

			setContentDisplay(
					ContentDisplay.GRAPHIC_ONLY);

		} else {

			setText(
					item == null
							? ""
							: item.toString());

			setContentDisplay(
					ContentDisplay.TEXT_ONLY);
		}
	}
}