package dev.shreyasayyengar.logininterface.util;

public enum AuthInfo {
    MYSQL_USERNAME,
    MYSQL_PASSWORD,
    MYSQL_DATABASE,
    MYSQL_HOST,
    MYSQL_PORT;

    private final String value;

    AuthInfo() {
        this.value = System.getenv(name());
    }

    public String get() {
        return value;
    }
}