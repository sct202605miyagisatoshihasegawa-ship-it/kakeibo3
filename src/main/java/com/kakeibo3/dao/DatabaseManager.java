package com.kakeibo3.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:kakeibo.db";

    private DatabaseManager() {
        // インスタンス化禁止
    }

    /**
     * アプリ起動時の初期化
     */
    public static void initialize() {
        createTransactionTable();
    }

    /**
     * DB接続取得
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * transactionsテーブル作成
     */
    private static void createTransactionTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    transaction_date TEXT NOT NULL,
                    amount INTEGER NOT NULL,
                    category TEXT NOT NULL,
                    payment_method TEXT NOT NULL,
                    detail TEXT
                )
                """;

        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException("DB初期化に失敗しました。", e);
        }
    }
}