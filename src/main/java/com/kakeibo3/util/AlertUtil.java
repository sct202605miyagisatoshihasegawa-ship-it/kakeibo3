package com.kakeibo3.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public final class AlertUtil {

	private AlertUtil() {
	}

	public static void showInfo(
			String title,
			String message) {

		Alert alert = new Alert(
				AlertType.INFORMATION);

		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}

	public static void showWarning(
			String title,
			String message) {

		Alert alert = new Alert(
				AlertType.WARNING);

		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}

	public static void showError(
			String title,
			String message) {

		Alert alert = new Alert(
				AlertType.ERROR);

		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}

	public static boolean showConfirm(
			String title,
			String message) {

		Alert alert = new Alert(
				AlertType.CONFIRMATION);

		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		Optional<ButtonType> result =
				alert.showAndWait();

		return result.isPresent()
				&& result.get() == ButtonType.OK;
	}

	/**
	 * 保存確認（保存・破棄・キャンセル）
	 */
	public static ConfirmResult showSaveDiscardCancel(
			String title,
			String message) {

		ButtonType saveButton =
				new ButtonType("保存して移動");

		ButtonType discardButton =
				new ButtonType("保存せず移動");

		ButtonType cancelButton =
				ButtonType.CANCEL;

		Alert alert =
				new Alert(AlertType.CONFIRMATION);

		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.getButtonTypes().setAll(
				saveButton,
				discardButton,
				cancelButton);

		Optional<ButtonType> result =
				alert.showAndWait();

		if (result.isEmpty()
				|| result.get() == cancelButton) {

			return ConfirmResult.CANCEL;
		}

		if (result.get() == saveButton) {

			return ConfirmResult.SAVE;
		}

		return ConfirmResult.DISCARD;
	}
}