package checkinn.dao;

import checkinn.database.MySqlConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoomDao {
    private final MySqlConnection dbConnection = new MySqlConnection();

    public void setRoomStatus(int roomId, int statusId) {
        String sql = "UPDATE Room SET status_id = ? WHERE room_id = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public void updateRoomPrice(int roomId, double newPrice) {
        String sql = "UPDATE Room SET price = ? WHERE room_id = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newPrice);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

