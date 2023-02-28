package dev.shreyasayyengar.logininterface;

import dev.shreyasayyengar.logininterface.database.MySQL;
import dev.shreyasayyengar.logininterface.gui.WelcomeFrame;
import dev.shreyasayyengar.logininterface.objects.LoginUser;
import dev.shreyasayyengar.logininterface.util.AuthInfo;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoginProgram {

    private static LoginProgram instance;

    private final Collection<LoginUser> loginUsers = new HashSet<>();
    private MySQL database;

    public static LoginProgram getInstance() {
        return instance;
    }

    public LoginProgram() {
        instance = this;
        initMySQL();
        loadExistingUsers();
        startGUI();
        shutdownHook();
    }

    private void initMySQL() {
        this.database = new MySQL(AuthInfo.MYSQL_USERNAME.get(), AuthInfo.MYSQL_PASSWORD.get(), AuthInfo.MYSQL_DATABASE.get(), AuthInfo.MYSQL_HOST.get(), Integer.parseInt(AuthInfo.MYSQL_PORT.get()));

        this.database.preparedStatementBuilder("create table if not exists login_users(" +
                "    uuid           varchar(36) not null," +
                "    username       tinytext    null," +
                "    password       tinytext    null," +
                "    email          tinytext    null," +
                "    address        tinytext    null," +
                "    gender         tinytext    null," +
                "    reservations   longtext    null" +
                ");").executeUpdate();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> this.database.preparedStatementBuilder("select 1").executeQuery(resultSet -> {
        }), 5, 5, TimeUnit.MINUTES); // keep-alive connection query
    }

    public void loadExistingUsers() {
        this.database.preparedStatementBuilder("select * from login_users").executeQuery(resultSet -> {
            try {
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    String address = resultSet.getString("address");
                    String gender = resultSet.getString("gender");
                    Collection<String> reservations = resultSet.getString("reservations") == null ? Collections.emptySet() : Arrays.stream(resultSet.getString("reservations").split("\\|")).toList();

                    loginUsers.add(new LoginUser(uuid, username, password, email, address, gender, new ArrayList<>(reservations)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void startGUI() {
        new WelcomeFrame();
    }

    private void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> loginUsers.forEach(LoginUser::serialise)));
    }

    // ---------------- Getters ----------------¬

    public MySQL getDatabase() {
        return database;
    }

    public Collection<LoginUser> getLoginUsers() {
        return loginUsers;
    }
}