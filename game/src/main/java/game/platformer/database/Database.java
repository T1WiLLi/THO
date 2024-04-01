package game.platformer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class Database {
    private Connection databaseConnection;

    public Database() {
        DatabaseConfig dbConfig = new DatabaseConfig();
        try {
            this.databaseConnection = DriverManager.getConnection(dbConfig.getDATABASE_URL(),
                    dbConfig.getDATABASE_USERNAME(), dbConfig.getDATABASE_PASSWORD());
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
            System.exit(0);
        }
    }

    public Connection getConnection() {
        return this.databaseConnection;
    }

    public boolean closeConnection() {
        try {
            if (this.databaseConnection != null) {
                this.databaseConnection.close();
                this.databaseConnection = null;
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public int getUserID(String username, String password) {
        String query = "SELECT id FROM app_user WHERE username = ? AND password = ?";
        try (PreparedStatement statement = this.databaseConnection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, User.hash(password));

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<Integer, Timestamp> getUserData(int playerId) {
        return null;
    }

    public boolean register(String username, String password) {
        return true;
    }
}
