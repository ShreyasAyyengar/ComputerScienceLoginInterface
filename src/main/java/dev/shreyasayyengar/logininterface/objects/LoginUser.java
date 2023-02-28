package dev.shreyasayyengar.logininterface.objects;

import dev.shreyasayyengar.logininterface.LoginProgram;
import dev.shreyasayyengar.logininterface.database.MySQL;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

public record LoginUser(UUID uuid, String username, String password, String email, String address, String gender, Collection<String> reservations) {

    public void serialise() {
        MySQL database = LoginProgram.getInstance().getDatabase();
        database.preparedStatementBuilder("select * from login_users where uuid = '" + uuid + "';").executeQuery(resultSet -> {
            try {
                if (!resultSet.next()) {
                    database.preparedStatementBuilder("insert into login_users (uuid, username, password, email, address, gender, reservations) values (?, ?, ?, ?, ?, ?, ?);")
                            .setString(1, uuid.toString())
                            .setString(2, username)
                            .setString(3, password)
                            .setString(4, email)
                            .setString(5, address)
                            .setString(6, gender)
                            .setString(7, reservations.isEmpty() ? null : String.join("|", reservations))
                            .executeUpdate();
                } else {
                    // only update their reservations
                    database.preparedStatementBuilder("update login_users set reservations = ? where uuid = ?;")
                            .setString(1, reservations.isEmpty() ? null : String.join("|", reservations))
                            .setString(2, uuid.toString())
                            .executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}