package com.kakeibo3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kakeibo3.model.CategoryType;
import com.kakeibo3.model.TransactionRecord;

public class TransactionDao {

	/**
	 * 全件取得
	 */
	public List<TransactionRecord> findAll() {

		String sql = """
				SELECT
				    transaction_date,
				    amount,
				    category,
				    payment_method,
				    detail
				FROM transactions
				ORDER BY transaction_date
				""";

		List<TransactionRecord> result = new ArrayList<>();

		try (
				Connection connection = DatabaseManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet rs = statement.executeQuery()) {

			while (rs.next()) {

				TransactionRecord record = new TransactionRecord(
						LocalDate.parse(
								rs.getString("transaction_date")),
						rs.getLong("amount"),
						CategoryType.valueOf(
								rs.getString("category")),
						rs.getString("payment_method"),
						rs.getString("detail"));

				result.add(record);
			}

		} catch (SQLException e) {
			throw new RuntimeException(
					"データ取得に失敗しました。", e);
		}

		return result;
	}

	/**
	 * 全件置換
	 */
	public void replaceAll(List<TransactionRecord> records) {

		String deleteSql = "DELETE FROM transactions";

		String insertSql = """
				INSERT INTO transactions (
				    transaction_date,
				    amount,
				    category,
				    payment_method,
				    detail
				)
				VALUES (?, ?, ?, ?, ?)
				""";

		try (
				Connection connection = DatabaseManager.getConnection()) {

			connection.setAutoCommit(false);

			try {

				// 全削除
				try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {

					deleteStmt.executeUpdate();
				}

				// 全挿入
				try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {

					for (TransactionRecord record : records) {

						insertStmt.setString(
								1,
								record.date().toString());

						insertStmt.setLong(
								2,
								record.amount());

						insertStmt.setString(
								3,
								record.category().name());

						insertStmt.setString(
								4,
								record.paymentMethod());

						insertStmt.setString(
								5,
								record.detail());

						insertStmt.executeUpdate();
					}
				}

				connection.commit();

			} catch (Exception e) {

				connection.rollback();
				throw e;
			}

		} catch (Exception e) {

			throw new RuntimeException(
					"データ保存に失敗しました。", e);
		}
	}

	/**
	 * 指定月のみ置換
	 */
	public void replaceMonth(
			int year,
			int month,
			List<TransactionRecord> records) {

		String targetMonth = String.format(
				"%04d-%02d",
				year,
				month);

		String deleteSql = """
				DELETE FROM transactions
				WHERE substr(
				        transaction_date,
				        1,
				        7
				) = ?
				""";

		String insertSql = """
				INSERT INTO transactions (
				    transaction_date,
				    amount,
				    category,
				    payment_method,
				    detail
				)
				VALUES (?, ?, ?, ?, ?)
				""";

		try (
				Connection connection = DatabaseManager.getConnection()) {

			connection.setAutoCommit(false);

			try {

				// 対象月削除
				try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {

					deleteStmt.setString(
							1,
							targetMonth);

					deleteStmt.executeUpdate();
				}

				// 対象月登録
				try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {

					for (TransactionRecord record : records) {

						insertStmt.setString(
								1,
								record.date().toString());

						insertStmt.setLong(
								2,
								record.amount());

						insertStmt.setString(
								3,
								record.category().name());

						insertStmt.setString(
								4,
								record.paymentMethod());

						insertStmt.setString(
								5,
								record.detail());

						insertStmt.executeUpdate();
					}
				}

				connection.commit();

			} catch (Exception e) {

				connection.rollback();
				throw e;
			}

		} catch (Exception e) {

			throw new RuntimeException(
					"月データ保存に失敗しました。",
					e);
		}

	}
}
