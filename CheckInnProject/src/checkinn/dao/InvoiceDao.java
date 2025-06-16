package checkinn.dao;

import checkinn.database.MySqlConnection;
import checkinn.model.Invoice;
import java.sql.*;

public class InvoiceDao {
    private final MySqlConnection dbConnection = new MySqlConnection();

    public int createInvoice(int bookingId, double totalAmount, String paymentMethod) {
        String sql = "INSERT INTO Invoice (booking_id, total_amount, invoice_date, payment_method) VALUES (?, ?, NOW(), ?)";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, bookingId);
            stmt.setDouble(2, totalAmount);
            stmt.setString(3, paymentMethod);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    public Invoice getInvoiceByBookingId(int bookingId) {
        String sql = "SELECT * FROM Invoice WHERE booking_id = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(rs.getInt("invoice_id"));
                invoice.setBookingId(rs.getInt("booking_id"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoice.setInvoiceDate(rs.getTimestamp("invoice_date"));
                invoice.setPaymentMethod(rs.getString("payment_method"));
                return invoice;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}