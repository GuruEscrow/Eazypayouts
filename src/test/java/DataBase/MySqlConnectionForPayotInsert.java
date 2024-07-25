package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlConnectionForPayotInsert {

	public static void insertPayoutToDB(String payout_ref, String payout_timestamp, String amt, String mode,
			String to_acc, String to_ifsc, String payoutResponse) throws SQLException, ClassNotFoundException {
		// JDBC URL, username, and password of the MySQL database
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/payouts?useSSL=false";
		String username = "Guruprasad";
		String password = "MySql@#123";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");
			String query = "INSERT INTO payouts.payouts_table (`payout_ref`, `payout_timestamp`, `amount`, `mode`, `to_acc_no`, `to_ifsc`, `payout_response`,`txn_type`)"
					+ "VALUES ('" + payout_ref + "', '" + payout_timestamp + "', '" + amt + "', '" + mode + "', '"
					+ to_acc + "', '" + to_ifsc + "', '" + payoutResponse + "', 'DR')";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}
	}

	public static void updateWebhookToPayoutTable(String webhook, String bankref, String payout_ref)
			throws ClassNotFoundException, SQLException {
		// JDBC URL, username, and password of the MySQL database
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/payouts?useSSL=false";
		String username = "Guruprasad";
		String password = "MySql@#123";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			// System.out.println("Connected to the database!");
			String query = "UPDATE payouts.payouts_table SET webhook_found = '" + webhook + "', bankref = '" + bankref
					+ "' WHERE payout_ref='" + payout_ref + "'";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Connection failed. Error: " + e.getMessage());
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (resultSet != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}
	}
	
	public static void insertPayoutWithoutWebhook(List<String> payoutDetails) throws ClassNotFoundException, SQLException {
		// JDBC URL, username, and password of the MySQL database
				String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/payouts?useSSL=false";
				String username = "Guruprasad";
				String password = "MySql@#123";
				
				String payout_ref=payoutDetails.get(0);
				String payout_timestamp=payoutDetails.get(1);
				String txn_type=payoutDetails.get(2);
				String amt=payoutDetails.get(3);
				String mode=payoutDetails.get(4);
				String to_acc=payoutDetails.get(5);
				String to_ifsc=payoutDetails.get(6);
				String payoutResponse=payoutDetails.get(7);
				
				Connection connection = null;
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				// Establishing the connection
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					connection = DriverManager.getConnection(jdbcUrl, username, password);
					// System.out.println("Connected to the database!");
					String query = "INSERT INTO payouts.payouts_without_webhook (`payout_ref`, `payout_timestamp`, `amount`, `mode`, `to_acc_no`, `to_ifsc`, `payout_response`,`txn_type`)"
							+ "VALUES ('" + payout_ref + "', '" + payout_timestamp + "', '" + amt + "', '" + mode + "', '"
							+ to_acc + "', '" + to_ifsc + "', '" + payoutResponse + "', '"+txn_type+"')";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.executeUpdate();

				} catch (SQLException e) {
					System.out.println("Connection failed. Error: " + e.getMessage());
				} finally {
					if (resultSet != null)
						resultSet.close();
					if (resultSet != null)
						preparedStatement.close();
					if (connection != null)
						connection.close();
				}
		
	}

}
