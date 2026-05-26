package my.code;

import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            var sql2 = "INSERT INTO users (username, phone) VALUES ('tommy', '123456789')";
            var sql3 = "SELECT * FROM users";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }
            try (var statement2 = conn.createStatement()) {
                statement2.executeUpdate(sql2);
            }
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql3);
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }
        }
    }
}
