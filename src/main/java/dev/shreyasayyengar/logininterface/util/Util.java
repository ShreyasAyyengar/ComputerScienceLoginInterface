package dev.shreyasayyengar.logininterface.util;

import dev.shreyasayyengar.logininterface.LoginProgram;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    public static boolean containsUserPassMatch(String username, String password) {
        return LoginProgram.getInstance().getLoginUsers().stream().anyMatch(loginUser -> loginUser.username().equals(username) && loginUser.password().equals(password));
    }
}