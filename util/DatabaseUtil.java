package util;

import java.sql.*;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/inventory_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";  
    private static final String PASSWORD = "ved@sqltext@2003";  
    private static Connection conn;
//Just a silly note- You can create a variable of an interface type, but you cannot create an object of an interface.
//In JDBC, the actual object is created by the JDBC driver, and it implements the Connection interface.
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                /* Internal working of DriverManager.getConnection:
                Internally:
                    DriverManager finds the registered JDBC driver (e.g., MySQL driver)
                    The driver creates its own connection object
                    That object implements java.sql.Connection
                    It returns it as a Connection reference
                Basically: In JDBC, Connection is an interface, and DriverManager.getConnection()
                returns an object of a driver-specific class that implements Connection, which is why the assignment(conn =DriverManager.getConnection.....)works.*/
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found", e);
            }
        }
        return conn;
    }

    public static void createSchema() throws SQLException {
        try (Connection c = getConnection();
             Statement s = c.createStatement()) {
            // Just create table - DB already in URL
            s.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    quantity INT NOT NULL DEFAULT 0,
                    price DECIMAL(10,2) NOT NULL DEFAULT 0.00
                )
                """);
            System.out.println("✅ MySQL table created/verified.");
        }
    }

    public static void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}


