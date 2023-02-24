package dev.shreyasayyengar.logininterface.gui;

import dev.shreyasayyengar.logininterface.util.Util;

import javax.swing.*;
import java.util.concurrent.Executors;

public class LoginFrame extends JFrame {

    private JPanel loginPanel;
    private JLabel successPredicateLabel;
    private JButton signInButton, forgotButton, backButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setContentPane(loginPanel);
        setSize(650, 350);
        setTitle("Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        signInButton.addActionListener(event -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            // check if empty
            if (username.isEmpty() || password.isEmpty()) {
                successPredicateLabel.setText("Login failed! Please fill in all the fields.");
                Executors.newSingleThreadScheduledExecutor().schedule(() -> successPredicateLabel.setText(""), 5, java.util.concurrent.TimeUnit.SECONDS);
                return;
            }

            if (Util.containsUserPassMatch(username, password)) {
                dispose();
                new MusicPlayerFrame();
            } else {
                successPredicateLabel.setText("Login failed! One or more of your credentials are incorrect.");
                Executors.newSingleThreadScheduledExecutor().schedule(() -> successPredicateLabel.setText(""), 5, java.util.concurrent.TimeUnit.SECONDS);
            }
        });

        forgotButton.addActionListener(event -> {
            dispose();
            new ForgotFrame();
        });

        backButton.addActionListener(event -> {
            dispose();
            new WelcomeFrame();
        });
    }
}