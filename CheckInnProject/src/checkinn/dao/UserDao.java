package checkinn.dao;

import checkinn.database.DbConnection;
import checkinn.database.MySqlConnection;
import checkinn.model.RegistrationRequest;
import checkinn.model.UserData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final DbConnection dbConnection;

    public UserDao() {
        this.dbConnection = new MySqlConnection();
    }

    public boolean authenticateUser(String email, String password) {
        String sql = "SELECT * FROM User WHERE email = ? AND password = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error during authentication: " + e.getMessage());
        }
        return false;
    }

    public boolean registerUser(RegistrationRequest request) {
        if (emailExists(request.getEmail())) {
            return false;
        }

        String sql = "INSERT INTO User (first_name, last_name, phone_number, email, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, request.getFirstName());
            pstmt.setString(2, request.getLastName());
            pstmt.setString(3, request.getPhoneNumber());
            pstmt.setString(4, request.getEmail());
            pstmt.setString(5, request.getPassword());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM User WHERE email = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }
        return false;
    }

    public boolean resetPassword(String email, String newPassword) {
        String sql = "UPDATE User SET password = ? WHERE email = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return false;
        }
    }

    public boolean createUser(UserData user) {
        String sql = "INSERT INTO User (first_name, last_name, phone_number, email, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPassword());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(UserData user) {
        String sql = "UPDATE User SET first_name = ?, last_name = ?, phone_number = ? WHERE user_id = ?";

        try (Connection conn = dbConnection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setInt(4, user.getUserId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    private UserData extractUserFromResultSet(ResultSet rs) throws SQLException {
        UserData user = new UserData();
        user.setUserId(rs.getInt("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhoneNumber(rs.getString("phone_number"));
        return user;
    }

    public UserData getUserByEmail(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
        }
        return null;
    }
    
    
    public boolean deleteUserByEmail(String email) {
    String sql = "DELETE FROM User WHERE email = ?";
    try (Connection conn = dbConnection.openConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, email);
        int rows = stmt.executeUpdate();
        return rows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    
    public List<UserData> getAllUsers() {
    List<UserData> users = new ArrayList<>();
    String sql = "SELECT * FROM User";
    try (Connection conn = dbConnection.openConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            UserData user = new UserData(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone_number")
            );
            users.add(user);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return users;
}
    
    
    public boolean deleteUserById(int userId) {
    String sql = "DELETE FROM User WHERE user_id = ?";
    try (Connection conn = dbConnection.openConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        int rows = stmt.executeUpdate();
        return rows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
}