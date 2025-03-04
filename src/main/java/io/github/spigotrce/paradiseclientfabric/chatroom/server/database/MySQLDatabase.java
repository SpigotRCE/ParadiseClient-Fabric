package io.github.spigotrce.paradiseclientfabric.chatroom.server.database;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.DatabaseModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Logging;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLDatabase {
    private final Connection connection;

    public MySQLDatabase() throws SQLException {
        DatabaseModel model = Main.CONFIG.getDatabase();
        this.connection = DriverManager.getConnection(
                "jdbc:mysql://" + model.host() + "/" + model.name(),
                model.username(),
                model.password()
        );
        Logging.info("Connected to MySQL database!");
        createTable();

    }

    private void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "discordID BIGINT PRIMARY KEY, " +
                "uuid CHAR(36) NOT NULL, " +
                "dateOfRegistration DATE NOT NULL, " +
                "username VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "token VARCHAR(255) NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
            Logging.info("Table 'users' created.");
        }
    }

    public void insertUser(UserModel user) throws SQLException {
        String query = "INSERT INTO users (discordID, uuid, dateOfRegistration, username, email, token) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, user.discordID());
            pstmt.setString(2, user.uuid().toString());
            pstmt.setDate(3, Date.valueOf(user.dateOfRegistration().toLocalDate()));
            pstmt.setString(4, user.username());
            pstmt.setString(5, user.email());
            pstmt.setString(6, user.token());
            pstmt.executeUpdate();
            Logging.info("Inserted user: " + user.username());
        }
    }

    public List<UserModel> getAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        List<UserModel> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new UserModel(
                        rs.getLong("discordID"),
                        UUID.fromString(rs.getString("uuid")),
                        rs.getDate("dateOfRegistration"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("token")
                ));
            }
        }
        return users;
    }

    public void updateUser(UserModel user) throws SQLException {
        String query = "UPDATE users SET uuid = ?, dateOfRegistration = ?, username = ?, email = ?, token = ? WHERE discordID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.uuid().toString());
            pstmt.setDate(2, Date.valueOf(user.dateOfRegistration().toLocalDate()));
            pstmt.setString(3, user.username());
            pstmt.setString(4, user.email());
            pstmt.setString(5, user.token());
            pstmt.setLong(6, user.discordID());
            pstmt.executeUpdate();
            Logging.info("Updated user: " + user.username());
        }
    }

    public void deleteUser(long discordID) throws SQLException {
        String query = "DELETE FROM users WHERE discordID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, discordID);
            pstmt.executeUpdate();
            Logging.info("Deleted user with Discord ID: " + discordID);
        }
    }
}
