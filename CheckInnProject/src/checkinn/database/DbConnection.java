package checkinn.database;

import java.sql.Connection;

public interface DbConnection {
    Connection openConnection();
    void closeConnection(Connection conn);
}