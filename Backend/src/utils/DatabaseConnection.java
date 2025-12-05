package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection initializeDatabase() throws Exception {
        String url = "jdbc:mysql://localhost:3306/cinebook_db";
        String username = "root";
        String password = "";

        // Load driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Return connection
        return DriverManager.getConnection(url, username, password);
    }
}
