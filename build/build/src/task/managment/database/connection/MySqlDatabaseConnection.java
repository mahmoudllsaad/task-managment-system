package task.managment.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDatabaseConnection {
	
	private static final String jdbcURL = "jdbc:mysql://localhost:3306/task_managment_system";
	private static final String username = "root";
	private static final String password = "";
	
	private static Connection connection = null;
	
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, username, password);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return connection;
	}

}
