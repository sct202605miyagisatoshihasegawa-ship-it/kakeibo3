package com.kakeibo3;



import com.kakeibo3.dao.DatabaseManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // DB初期化
        try {
            DatabaseManager.initialize();

        } catch (RuntimeException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("起動エラー");
            alert.setHeaderText("データベース初期化エラー");
            alert.setContentText(e.getMessage());

            alert.showAndWait();

            Platform.exit();
            return;
        }
        

        // 1. FXMLをロードして親ノード（root）を取得する
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/fxml/KakeiboView.fxml"));

        Parent root = loader.load();

        // 2. rootとサイズ（幅1200, 高さ675）を指定してSceneを作成する
        Scene scene = new Scene(root, 1200, 675);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            // TAB / Shift+TAB を無効化
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                return;
            }

            // Ctrlショートカットを無効化
            if (event.isControlDown()) {
                event.consume();
                return;
            }

            // Altキーを含む操作を無効化
            if (event.isAltDown()) {
                event.consume();
                return;
            }

            // Functionキー（F1～F12）を無効化
            if (event.getCode().isFunctionKey()) {
                event.consume();
                return;
            }

        });
        
     // 右クリックを無効化
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                event.consume();
            }
        });

     stage.setTitle("家計簿3");
     stage.setScene(scene);

     stage.show();
     }

     public static void main(String[] args) {
         launch(args);
     }
     }