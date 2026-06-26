package com.kakeibo3.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.kakeibo3.controller.cell.AmountTableCell;
import com.kakeibo3.controller.cell.DateTableCell;
import com.kakeibo3.controller.cell.DetailTableCell;
import com.kakeibo3.dao.TransactionDao;
import com.kakeibo3.model.CategoryType;
import com.kakeibo3.model.PaymentMethodType;
import com.kakeibo3.model.TransactionProperty;
import com.kakeibo3.model.TransactionRecord;
import com.kakeibo3.util.AlertUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.ComboBoxTableCell;

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
	private TableColumn<TransactionProperty, PaymentMethodType> colPaymentMethod;

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

	private final TransactionDao transactionDao = new TransactionDao();

	private void saveCurrentMonth() {
		
		System.out.println(
				"saveCurrentMonth START");

		// ----------------------------
		// 年間タブは保存不可
		// ----------------------------
		
		transactionTable.requestFocus();

		if (selectedMonth == null) {

			AlertUtil.showWarning(
					"保存",
					"年間タブは保存できません。\n"
							+ "月タブを選択してください。");

			return;
		}

		// ----------------------------
		// 保存確認
		// ----------------------------



		if (!AlertUtil.showConfirm(
				"保存",
				selectedYear + "年"
						+ selectedMonth
						+ "月のデータを保存しますか？")) {

			return;
		}
		

		try {

			// ----------------------------
			// 年月整合性チェック
			// ----------------------------

			for (TransactionProperty item : transactionTable.getItems()) {

				LocalDate date = item.getDate();

				if (date == null
						|| date.getYear() != selectedYear
						|| date.getMonthValue() != selectedMonth) {

					AlertUtil.showWarning(
							"保存エラー",
							"選択中の年月以外のデータが含まれています。\n"
									+ "日付を確認してください。");

					return;
				}
			}

			// ----------------------------
			// TransactionRecord変換
			// ----------------------------

			List<TransactionRecord> records = transactionTable.getItems()
					.stream()
					.map(TransactionProperty::toRecord)
					.toList();

			// ----------------------------
			// DB保存
			// ----------------------------

			transactionDao.replaceMonth(
					selectedYear,
					selectedMonth,
					records);

			// ----------------------------
			// 保存完了
			// ----------------------------

			AlertUtil.showInfo(
					"保存",
					"保存しました。");

		} catch (Exception e) {

			e.printStackTrace();

			AlertUtil.showError(
					"保存エラー",
					"保存に失敗しました。\n"
							+ e.getMessage());
		}
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

		colDate.setCellValueFactory(cellData ->

		cellData.getValue().dateProperty());

		// ----------------------------
		// 日付列編集
		// ----------------------------

		colDate.setCellFactory(
				column -> new DateTableCell(
						() -> selectedYear,
						() -> selectedMonth));

		colDate.setOnEditCommit(event -> {

			TransactionProperty item = event.getRowValue();

			LocalDate newValue = event.getNewValue();

			if (newValue == null) {

				transactionTable.refresh();
				return;
			}

			item.setDate(newValue);

			System.out.println(
					"日付編集 : " +
							newValue);
		});

		colAmount.setCellValueFactory(cellData ->

		cellData.getValue().amountProperty().asObject());

		colCategory.setCellValueFactory(cellData ->

		cellData.getValue().categoryProperty());

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

		colPaymentMethod.setCellValueFactory(
				cellData -> cellData.getValue().paymentMethodProperty());

		// ----------------------------
		// 決済方法列編集
		// ----------------------------

		System.out.println(
				"PaymentMethod CellFactory SET");

		colPaymentMethod.setCellFactory(
				ComboBoxTableCell.forTableColumn(
						PaymentMethodType.values()));

		colPaymentMethod.setOnEditCommit(event -> {

			TransactionProperty item = event.getRowValue();

			item.setPaymentMethod(
					event.getNewValue());

			System.out.println(
					"決済方法編集 : " +
							event.getNewValue());
		});

		colDetail.setCellValueFactory(
				cellData -> cellData.getValue().detailProperty());

		// ----------------------------
		// 内容列編集
		// ----------------------------

		transactionTable.setEditable(true);

		colDetail.setCellFactory(
				column -> new DetailTableCell());

		colDetail.setOnEditCommit(event -> {

			TransactionProperty item = event.getRowValue();

			item.setDetail(
					event.getNewValue());

			System.out.println(
					"内容編集 : " +
							event.getNewValue());
		});

		//----------------------------
		//金額列編集
		//----------------------------

		colAmount.setCellFactory(
				column -> new AmountTableCell());

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

		saveButton.setOnAction(
				event -> saveCurrentMonth());

		// ----------------------------
		// TABキー無効
		// ----------------------------

		transactionTable.addEventFilter(
				javafx.scene.input.KeyEvent.KEY_PRESSED,
				event -> {

					if (event.getCode()
							== javafx.scene.input.KeyCode.TAB) {

						event.consume();
					}
				});

		System.out.println("KakeiboController initialized");
	}

	private void refreshTable() {

		List<TransactionRecord> records = transactionDao.findAll();

		if (selectedMonth != null) {

			records = records.stream()
					.filter(record -> record.date().getYear() == selectedYear
							&&
							record.date().getMonthValue() == selectedMonth)
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

}