package Utils;

import java.sql.*;

public class DatabaseConnection {

    public static String getEmail;
    public static String getPassword;
    public static String getFirstName;

    public static void dbConnection() {
        // Updated URL for MariaDB/MySQL
        String dbURL = "jdbc:mysql://102.222.124.22:3306/ndosian6b8b7_teaching";
        String dbUsername = "ndosian6b8b7_teaching";
        String dbPassword = "^{SF0a=#~[~p)@l1";

        try (Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword)) {

            // Querying the 'users' table we just created
            String query = "SELECT * FROM users WHERE email = 'admin@gmail.com'";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                if (resultSet.next()) {
                    getEmail = resultSet.getString("email");
                    getPassword = resultSet.getString("password");
                    getFirstName = resultSet.getString("firstName");

                    System.out.println("--- DB Connection Successful ---");
                    System.out.println("Fetched Admin -> Email: " + getEmail + ", Name: " + getFirstName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }
    }
}