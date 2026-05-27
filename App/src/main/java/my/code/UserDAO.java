package my.code;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Optional;

public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public void save (User user) throws SQLException {
        if (user.getId() == null) {
            var query = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try(var statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getName());
                statement.setString(2, user.getPhone());
                statement.executeUpdate();
                var keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                }
            }
        } else {
            var query = "UPDATE users SET username = ?, phone = ? WHERE id = ?";
            try (var statement = conn.prepareStatement(query)) {
                statement.setLong(3, user.getId());
                statement.setString(1, user.getName());
                statement.setString(2, user.getPhone());
                statement.executeUpdate();
            }
        }

    }

    public int delete(Long id) throws SQLException {
        try (var statement = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        }
    }

    public Optional<User> find(Long id) throws SQLException{
        var query = "SELECT * FROM users WHERE id = ?";
        try (var st = conn.prepareStatement(query)) {
            st.setLong(1, id);
            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    var name = rs.getString("username");
                    var phone = rs.getString("phone");
                    var user = new User(name, phone);
                    user.setId(id);
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        }
    }
}
