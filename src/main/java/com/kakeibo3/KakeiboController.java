package com.kakeibo3;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

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


public class KakeiboController implements Initializable {
	
	@FXML
	private Label bankABalanceLabel;

	@FXML
	private Label bankBBalanceLabel;

	@FXML
	private Label cashBalanceLabel;
	
	private List<ToggleButton> monthButtons;

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

    // ----------------------------
    // 明細一覧
    // ----------------------------

    @FXML
    private TableView<?> transactionTable;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colCategory;

    @FXML
    private TableColumn<?, ?> colPaymentMethod;

    @FXML
    private TableColumn<?, ?> colDetail;

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
            summaryTitleLabel.setText("年間収支内訳");
        } else {
            summaryTitleLabel.setText(text + "収支内訳");
        }

        System.out.println("選択タブ : " + text);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	

        Objects.requireNonNull(yearButton);
        Objects.requireNonNull(transactionTable);
        Objects.requireNonNull(summaryTitleLabel);

        yearButton.setSelected(true);

        summaryTitleLabel.setText("年間収支内訳");
        
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
                decButton
        );
        
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
                                        .add(25)
                        )
        );

        System.out.println("KakeiboController initialized");
    }
}