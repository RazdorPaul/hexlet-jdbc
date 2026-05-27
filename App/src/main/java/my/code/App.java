package my.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:dao_test")) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE users (id BIGINT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255), phone VARCHAR(255))");
            }
            var dao = new UserDAO(conn);
            var user = new User("Alice", "+70001112233");
            dao.save(user);
            System.out.println("Saved userId " + user.getId());
            var searchResult = dao.find(user.getId());
            searchResult.ifPresent(u -> System.out.println("Name " + u.getName() + ", phone " + u.getPhone()));
            if (searchResult.isEmpty()) {
                System.out.println("Пользователь не найден");
            }
            var deleted = dao.delete(user.getId());
            System.out.println("из базы удалено " + deleted + " пользователей");
            searchResult = dao.find(user.getId());
            searchResult.ifPresent(u -> System.out.println("Name " + u.getName() + ", phone " + u.getPhone()));
            if (searchResult.isEmpty()) {
                System.out.println("Пользователь не найден");
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
