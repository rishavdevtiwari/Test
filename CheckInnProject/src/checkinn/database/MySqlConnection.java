package checkinn.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlConnection implements DbConnection {
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "CheckInndb";
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "Coventry2019@";

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String SERVER_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";

    @Override
    public Connection openConnection() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);

            // First connect to server to ensure database exists
            try (Connection serverConn = DriverManager.getConnection(SERVER_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = serverConn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                System.out.println("Database '" + DB_NAME + "' ensured to exist.");
            }

            // Now connect to the specific database
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Successfully connected to database '" + DB_NAME + "'.");

            // Initialize tables if needed
            initializeTables(conn);

            return conn;

        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Connection to database '" + DB_NAME + "' failed: " + e.getMessage());
        }
        return conn;
    }

    private void initializeTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String[] createTables = {
                "CREATE TABLE IF NOT EXISTS Statuses (" +
                "status_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "status_info VARCHAR(100) NOT NULL)",

                "CREATE TABLE IF NOT EXISTS User (" +
                "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "first_name VARCHAR(50) NOT NULL, " +
                "last_name VARCHAR(50) NOT NULL, " +
                "phone_number VARCHAR(20), " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL)",

                "CREATE TABLE IF NOT EXISTS Admin (" +
                "admin_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "status_id INT, " +
                "FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (status_id) REFERENCES Statuses(status_id) ON DELETE SET NULL ON UPDATE CASCADE)",

                "CREATE TABLE IF NOT EXISTS Room (" +
                "room_id INT PRIMARY KEY not null, " +
                "price decimal(15,2),"+
                "description TEXT, " +
                "image_path varchar(150),"+
                "status_id INT default 1, " +
                "FOREIGN KEY (status_id) REFERENCES Statuses(status_id) ON DELETE SET NULL ON UPDATE CASCADE)",

                "CREATE TABLE IF NOT EXISTS MenuItem (" +
                "menu_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "itemname VARCHAR(100) NOT NULL, " +
                "price DECIMAL(10,2) NOT NULL)",

                "CREATE TABLE IF NOT EXISTS Booking (" +
                "booking_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "room_id INT NOT NULL, " +
                "user_id INT NOT NULL, " +
                "admin_id INT, " +
                "status_id INT, " +
                "menu_id INT, " +
                "invoice_id INT, " +
                "CheckIn_date DATETIME NOT NULL, " +
                "CheckOut_date DATETIME NOT NULL, " +
                "total_price Decimal(10,2),"+
                "FOREIGN KEY (room_id) REFERENCES Room(room_id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (status_id) REFERENCES Statuses(status_id) ON DELETE SET NULL ON UPDATE CASCADE, " +
                "FOREIGN KEY (menu_id) REFERENCES MenuItem(menu_id) ON DELETE SET NULL ON UPDATE CASCADE)",

                "CREATE TABLE IF NOT EXISTS Invoice (" +
                "invoice_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "booking_id INT NOT NULL, " +
                "total_amount DECIMAL(10, 2) NOT NULL, " +
                "invoice_date DATETIME NOT NULL, " +
                "payment_method VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (booking_id) REFERENCES Booking(booking_id) ON DELETE CASCADE ON UPDATE CASCADE)"
            };

            for (String sql : createTables) {
                stmt.execute(sql);
            }

        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Statuses");
        rs.next();

        if (rs.getInt(1) == 0) {
            stmt.executeUpdate("INSERT INTO Statuses (status_id, status_info) VALUES " +
                              "(1, 'Vacant'), (2, 'Occupied'), (3, 'Out of Order')");
        }

        rs = stmt.executeQuery("SELECT COUNT(*) FROM MenuItem");
        rs.next();
        if (rs.getInt(1) == 0) {
            stmt.executeUpdate(
                "INSERT INTO MenuItem (menu_id, itemname, price) VALUES " +
                "(1, 'Breakfast Package', 500.00), " +
                "(2, 'Brunch Package', 850.00), " +
                "(3, 'Lunch Package', 1000.00), " +
                "(4, 'Dinner Package', 1500.00)"
            );
        }

        rs = stmt.executeQuery("SELECT COUNT(*) FROM Room");
        rs.next();
        if (rs.getInt(1) == 0) {
            stmt.executeUpdate(
                "INSERT INTO Room (room_type, price, description, image_path, status_id) VALUES " +
                "('Single Room', 2000.00, 'Perfect for the solo traveler...', '/images/singleroom.jpg', 1), " +
                "('Double Room', 3000.00, 'Ideal for couples or friends...', '/images/doubleroom.jpg', 1), " +
                "('Deluxe Room', 5000.00, 'Indulge in an elevated experience...', '/images/Deluxe.jpg', 1), " +
                "('Executive Suite', 8000.00, 'Experience the pinnacle of luxury...', '/images/executive room.jpg', 1)"
            );
        }

        if (!foreignKeyExists(conn, DB_NAME, "Booking", "fk_booking_invoice")) {
            stmt.execute("ALTER TABLE Booking ADD CONSTRAINT fk_booking_invoice " +
                        "FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id) " +
                        "ON DELETE SET NULL ON UPDATE CASCADE");
        }

            System.out.println("All tables initialized successfully.");
        }
    }

    private boolean foreignKeyExists(Connection conn, String dbName, String tableName, String fkName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS " +
                     "WHERE CONSTRAINT_SCHEMA = ? AND TABLE_NAME = ? " +
                     "AND CONSTRAINT_NAME = ? AND CONSTRAINT_TYPE = 'FOREIGN KEY'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dbName);
            pstmt.setString(2, tableName);
            pstmt.setString(3, fkName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection to '" + DB_NAME + "' closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
}
    }
}