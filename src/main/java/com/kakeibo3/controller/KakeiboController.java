package com.kakeibo3.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.kakeibo3.dao.TransactionDao;
import com.kakeibo3.model.CategoryType;
import com.kakeibo3.model.PaymentMethodType;
import com.kakeibo3.model.TransactionProperty;
import com.kakeibo3.model.TransactionRecord;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

public class KakeiboController implements Initializable {

	@FXML
	private Label bankABalanceLabel;

	@FXML
	private Label bankBBalanceLabel;

	@FXML
	private Label cashBalanceLabel;

	private List<ToggleButton> monthButtons;

	// null = 年間表示
	private Integer selectedMonth = null;

	// 現在選択中の年
	private int selectedYear = LocalDate.now().getYear();

	// ----------------------------
	// 上部メニュー
	// ----------------------------

	@FXML
	private ToggleGroup tabGroup;

	@FXML
	private ToggleButton yearButton;

	@FXML
	private ToggleButton janButton;

	@FXML
	private ToggleButton febButton;

	@FXML
	private ToggleButton marButton;

	@FXML
	private ToggleButton aprButton;

	@FXML
	private ToggleButton mayButton;

	@FXML
	private ToggleButton junButton;

	@FXML
	private ToggleButton julButton;

	@FXML
	private ToggleButton augButton;

	@FXML
	private ToggleButton sepButton;

	@FXML
	private ToggleButton octButton;

	@FXML
	private ToggleButton novButton;

	@FXML
	private ToggleButton decButton;

	@FXML
	private Button saveButton;

	@FXML
	private Button undoButton;
	@FXML
	private TableView<TransactionProperty> transactionTable;

	@FXML
	private TableColumn<TransactionProperty, LocalDate> colDate;

	@FXML
	private TableColumn<TransactionProperty, Long> colAmount;

	@FXML
	private TableColumn<TransactionProperty, CategoryType> colCategory;

	@FXML
	private TableColumn<
	        TransactionProperty,
	        PaymentMethodType>
	        colPaymentMethod;

	@FXML
	private TableColumn<TransactionProperty, String> colDetail;
	// ----------------------------
	// 収支内訳
	// ----------------------------

	@FXML
	private Label summaryTitleLabel;

	@FXML
	private ListView<?> categorySummaryList;

	// ----------------------------
	// メモ
	// ----------------------------

	@FXML
	private TextArea memoArea;

	@FXML
	private void handleMonthSelection(ActionEvent event) {

		Toggle selected = tabGroup.getSelectedToggle();

		if (!(selected instanceof ToggleButton button)) {
			return;
		}

		String text = button.getText();

		if ("年間".equals(text)) {

			selectedMonth = null;
			summaryTitleLabel.setText("年間収支内訳");

		} else {

			selectedMonth = Integer.parseInt(
					text.replace("月", ""));

			summaryTitleLabel.setText(text + "収支内訳");
		}

		refreshTable();

		System.out.println(
				"選択タブ : " + text +
						" / 月=" + selectedMonth);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Objects.requireNonNull(yearButton);
		Objects.requireNonNull(transactionTable);
		Objects.requireNonNull(summaryTitleLabel);

		yearButton.setSelected(true);

		summaryTitleLabel.setText("年間収支内訳");

		// ----------------------------
		// TableView列設定
		// ----------------------------

		colDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

		colAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());

		colCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

		// ----------------------------
		// カテゴリ列編集
		// ----------------------------

		colCategory.setCellFactory(
				ComboBoxTableCell.forTableColumn(
						CategoryType.values()));

		colCategory.setOnEditCommit(event -> {

			TransactionProperty item = event.getRowValue();

			item.setCategory(
					event.getNewValue());

			System.out.println(
					"カテゴリ編集 : " +
							event.getNewValue());
		});

		colPaymentMethod.setCellValueFactory(cellData -> cellData.getValue().paymentMethodProperty());

		colDetail.setCellValueFactory(cellData -> cellData.getValue().detailProperty());

		// ----------------------------
		// 内容列編集
		// ----------------------------

		transactionTable.setEditable(true);

		colDetail.setCellFactory(
				TextFieldTableCell.forTableColumn());

		colDetail.setOnEditCommit(event -> {

			TransactionProperty item = event.getRowValue();

			item.setDetail(event.getNewValue());

			System.out.println(
					"内容編集 : " +
							event.getNewValue());
		});

		//----------------------------
		//金額列編集
		//----------------------------

		colAmount.setCellFactory(
				column -> createNumericCell());

		colAmount.setOnEditCommit(event -> {

			TransactionProperty item = event.getRowValue();

			Long newValue = event.getNewValue();

			if (newValue == null) {

				System.out.println(
						"金額編集失敗 : null");

				transactionTable.refresh();
				return;
			}

			item.setAmount(newValue);

			System.out.println(
					"金額編集 : " +
							newValue);
		});

		// ----------------------------
		// DB読込
		// ----------------------------

		refreshTable();

		monthButtons = List.of(
				janButton,
				febButton,
				marButton,
				aprButton,
				mayButton,
				junButton,
				julButton,
				augButton,
				sepButton,
				octButton,
				novButton,
				decButton);

		Objects.requireNonNull(bankABalanceLabel);
		Objects.requireNonNull(bankBBalanceLabel);
		Objects.requireNonNull(cashBalanceLabel);

		colDetail.prefWidthProperty().bind(
				transactionTable.widthProperty()
						.subtract(
								colDate.widthProperty()
										.add(colAmount.widthProperty())
										.add(colCategory.widthProperty())
										.add(colPaymentMethod.widthProperty())
										.add(25)));

		System.out.println("KakeiboController initialized");
	}

	private void refreshTable() {

		TransactionDao dao = new TransactionDao();

		List<TransactionRecord> records = dao.findAll();

		if (selectedMonth != null) {

			records = records.stream()
					.filter(record -> record.date()
							.getMonthValue() == selectedMonth)
					.toList();
		}

		ObservableList<TransactionProperty> items = FXCollections.observableArrayList(
				records.stream()
						.map(TransactionProperty::new)
						.toList());

		transactionTable.setItems(items);

		// ----------------------------
		// 年間タブは編集禁止
		// ----------------------------

		transactionTable.setEditable(
				selectedMonth != null);

		System.out.println(
				"表示件数 = " + items.size());

		System.out.println(
				"編集可 = " +
						transactionTable.isEditable());
	}

	private TableCell<TransactionProperty, Long> createNumericCell() {

		return new TableCell<>() {

			private final TextField textField = new TextField();

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

			{
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

				textField.setOnAction(event -> commitCurrentValue());

				textField.focusedProperty()
						.addListener((obs,
								oldValue,
								newValue) -> {

							if (!newValue) {

								commitCurrentValue();
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

				super.updateItem(item, empty);

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
		};
	}

}