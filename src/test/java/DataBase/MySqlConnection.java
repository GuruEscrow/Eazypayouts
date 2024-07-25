package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MySqlConnection {

	public static boolean webhookChecker(String payout_ref) throws SQLException, ClassNotFoundException {
		// JDBC URL, username, and password of the MySQL database
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/payouts?useSSL=false";
		String username = "Guruprasad";
		String password = "MySql@#123";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean flag=false;
		// Establishing the connection
		try {
			List<String> payoutDetails=getPayoutTimestamp(payout_ref);
			String payoutTimestamp=payoutDetails.get(1);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			String query = "SELECT * FROM payouts.payout_webhooks where payout_ref='" + payout_ref + "'";

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			int i = 1;
			while (resultSet.next()) {
				String webhook = "Yes";
				String bankref = resultSet.getString("bankref");
				MySqlConnectionForPayotInsert.updateWebhookToPayoutTable(webhook, bankref, payout_ref);
				i++;
				flag=true;
			}
			if(i==1) {
				DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime payoutDate=LocalDateTime.parse(payoutTimestamp,formatter);
				String date=LocalDateTime.now().format(formatter);
				LocalDateTime currentDate=LocalDateTime.parse(date,formatter);
				long delay=ChronoUnit.SECONDS.between(payoutDate, currentDate);
				if(delay>3600) {
					MySqlConnectionForPayotInsert.insertPayoutWithoutWebhook(payoutDetails);
					flag=true;
				}else {
					flag=false;
				}
			}

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
		return flag;
	}

	public static List<String> getPayoutTimestamp(String payout_ref) throws ClassNotFoundException, SQLException {
		// JDBC URL, username, and password of the MySQL database
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/payouts?useSSL=false";
		String username = "Guruprasad";
		String password = "MySql@#123";
		List <String>payoutDetails=new ArrayList<String>();
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Establishing the connection
		String payout_timeStamp=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			String query = "SELECT * FROM payouts.payouts_table where payout_ref='" + payout_ref + "'";

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
		
			
			while (resultSet.next()) {
				payoutDetails.add(resultSet.getString("payout_ref"));
				payoutDetails.add(resultSet.getString("payout_timestamp"));
				payoutDetails.add(resultSet.getString("txn_type"));
				payoutDetails.add(resultSet.getString("amount"));
				payoutDetails.add(resultSet.getString("mode"));
				payoutDetails.add(resultSet.getString("to_acc_no"));
				payoutDetails.add(resultSet.getString("to_ifsc"));
				payoutDetails.add(resultSet.getString("payout_response"));
			
			}
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
		return payoutDetails;

	}

}
