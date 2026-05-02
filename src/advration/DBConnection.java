package advration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection {
    public static Connection getConnection() {
        try {
            // Update these credentials if your local MySQL setup is different
            String url = "jdbc:mysql://localhost:3306/rationdb";
            String user = "root";
            String password = "Thejaswini@242005"; 
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("Database Connection Failed: " + e.getMessage());
            return null;
        }
    }
}
