package com.kakeibo3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // 1. FXMLをロードして親ノード（root）を取得する
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/KakeiboView.fxml"));
        Parent root = loader.load();

        // 2. rootとサイズ（幅1200, 高さ675）を指定してSceneを作成する
        Scene scene = new Scene(root, 1200, 675);

        stage.setTitle("家計簿3");
        stage.setScene(scene);

        // ※ stage.setWidth / setHeight は scene のサイズ指定と重複するため削除しました

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
