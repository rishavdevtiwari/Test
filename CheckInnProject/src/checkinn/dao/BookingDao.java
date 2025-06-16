package checkinn.dao;

import checkinn.database.MySqlConnection;
import checkinn.model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingDao {
    private final MySqlConnection dbConnection = new MySqlConnection();

public int saveBooking(Booking booking) {
    String sql = "INSERT INTO Booking (room_id, user_id, menu_id, status_id, CheckIn_date, CheckOut_date, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = dbConnection.openConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, booking.getRoomId());
        stmt.setInt(2, booking.getUserId());
        stmt.setInt(3, booking.getMenuId());
        stmt.setInt(4, booking.getStatusId());
        stmt.setTimestamp(5, new java.sql.Timestamp(booking.getCheckInDate().getTime()));
        stmt.setTimestamp(6, new java.sql.Timestamp(booking.getCheckOutDate().getTime()));
        stmt.setDouble(7, booking.getTotalPrice());
        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 0) return -1;
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1;
}

public void updateInvoiceId(int bookingId, int invoiceId) {
    String sql = "UPDATE Booking SET invoice_id = ? WHERE booking_id = ?";
    try (Connection conn = dbConnection.openConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, invoiceId);
        stmt.setInt(2, bookingId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public List<Booking> getBookingsForUser(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Booking WHERE user_id = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setRoomId(rs.getInt("room_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setMenuId(rs.getInt("menu_id"));
                booking.setStatusId(rs.getInt("status_id"));
                booking.setInvoiceId(rs.getInt("invoice_id"));
                booking.setCheckInDate(rs.getTimestamp("CheckIn_date"));
                booking.setCheckOutDate(rs.getTimestamp("CheckOut_date"));
                booking.setTotalPrice(rs.getDouble("total_price"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Integer> getMenuIdsForBooking(int userId, int roomId, Date checkIn, Date checkOut) {
        List<Integer> menuIds = new ArrayList<>();
        String sql = "SELECT menu_id FROM Booking WHERE user_id = ? AND room_id = ? AND CheckIn_date = ? AND CheckOut_date = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, roomId);
            stmt.setTimestamp(3, new java.sql.Timestamp(checkIn.getTime()));
            stmt.setTimestamp(4, new java.sql.Timestamp(checkOut.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                menuIds.add(rs.getInt("menu_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuIds;
    }

    public List<Booking> getBookingsForUserRoomAndDates(int userId, int roomId, Date checkIn, Date checkOut) {
    List<Booking> bookings = new ArrayList<>();
    String sql = "SELECT * FROM Booking WHERE user_id = ? AND room_id = ? AND CheckIn_date = ? AND CheckOut_date = ?";
    try (Connection conn = dbConnection.openConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        stmt.setInt(2, roomId);
        stmt.setTimestamp(3, new java.sql.Timestamp(checkIn.getTime()));
        stmt.setTimestamp(4, new java.sql.Timestamp(checkOut.getTime()));
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Booking booking = new Booking();
            booking.setBookingId(rs.getInt("booking_id"));
            booking.setRoomId(rs.getInt("room_id"));
            booking.setUserId(rs.getInt("user_id"));
            booking.setMenuId(rs.getInt("menu_id"));
            booking.setStatusId(rs.getInt("status_id"));
            booking.setInvoiceId(rs.getInt("invoice_id"));
            booking.setCheckInDate(rs.getTimestamp("CheckIn_date"));
            booking.setCheckOutDate(rs.getTimestamp("CheckOut_date"));
            booking.setTotalPrice(rs.getDouble("total_price"));
            bookings.add(booking);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return bookings;
}


}