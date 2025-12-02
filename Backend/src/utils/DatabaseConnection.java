package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection initializeDatabase() throws Exception {
        String dbDriver = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/";
        String dbName = "cinebook_db"; // ganti sesuai nama DB kamu
        String dbUsername = "root";
        String dbPassword = ""; // kosong untuk XAMPP

        Class.forName(dbDriver);
        connection = DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword);
        return connection;
    }
}
