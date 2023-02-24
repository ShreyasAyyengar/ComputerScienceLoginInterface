package dev.shreyasayyengar.logininterface.gui;

import dev.shreyasayyengar.logininterface.LoginProgram;
import dev.shreyasayyengar.logininterface.objects.LoginUser;
import dev.shreyasayyengar.logininterface.util.Util;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.UUID;

public class SignUpFrame extends JFrame {
    private JPanel signUpPanel;
    private JLabel usernameResult, passwordResult, confirmPasswordResult, addressResult, emailResult, genderResult;
    private JTextField usernameField, emailField, genderField, addressField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton signUpButton, backButton;
    private boolean isUsernameAcceptable = false, isPasswordAcceptable = false, isConfirmPasswordAcceptable = false, isAddressAcceptable = false, isEmailAcceptable = false, isGenderAcceptable = false;

    public SignUpFrame() {
        setContentPane(signUpPanel);
        setSize(500, 500);
        setTitle("Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        LoginProgram instance = LoginProgram.getInstance();

        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typedUsername = usernameField.getText() + e.getKeyChar();

                if (typedUsername.isEmpty()) {
                    setAcceptable(usernameResult, false, "Empty!");
                    return;
                }

                instance.getLoginUsers().forEach(loginUser -> {

                    if (typedUsername.equalsIgnoreCase(loginUser.username())) {
                        setAcceptable(usernameResult, false, "Already exists!");
                    } else {
                        setAcceptable(usernameResult, true, null);
                        isUsernameAcceptable = true;
                    }
                });
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typedPassword = String.valueOf(passwordField.getPassword()) + e.getKeyChar();

                if (typedPassword.isEmpty() || typedPassword.isBlank()) {
                    setAcceptable(passwordResult, false, "Empty!");
                    return;
                }

                // length over 8
                if (typedPassword.length() < 8) {
                    setAcceptable(passwordResult, false, "Too short!");
                    return;
                }

                // contains capital letter
                if (!typedPassword.matches(".*[A-Z].*")) {
                    setAcceptable(passwordResult, false, "No capital letter!");
                    return;
                }

                // contains number
                if (!typedPassword.matches(".*[0-9].*")) {
                    setAcceptable(passwordResult, false, "No number!");
                    return;
                }

                // contains special character
                if (!typedPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                    setAcceptable(passwordResult, false, "No special character!");
                    return;
                }

                setAcceptable(passwordResult, true, null);
                isPasswordAcceptable = true;
            }
        });

        confirmPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typedPassword = String.valueOf(passwordField.getPassword());
                String typedConfirmPassword = String.valueOf(confirmPasswordField.getPassword()) + e.getKeyChar();

                if (typedConfirmPassword.isEmpty()) {
                    setAcceptable(confirmPasswordResult, false, "Empty!");
                    return;
                }

                if (!typedPassword.equals(typedConfirmPassword)) {
                    setAcceptable(confirmPasswordResult, false, "Does not match!");
                    return;
                }

                setAcceptable(confirmPasswordResult, true, null);
                isConfirmPasswordAcceptable = true;
            }
        });

        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typedEmail = emailField.getText() + e.getKeyChar();

                if (typedEmail.isEmpty()) {
                    setAcceptable(emailResult, false, "Empty!");
                    return;
                }

                if (!Util.validate(typedEmail)) {
                    setAcceptable(emailResult, false, "Invalid!");
                    return;
                }

                setAcceptable(emailResult, true, null);
                isEmailAcceptable = true;
            }
        });

        addressField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typedAddress = addressField.getText() + e.getKeyChar();

                if (typedAddress.isEmpty()) {
                    setAcceptable(addressResult, false, "Empty!");
                    return;
                }

                if (typedAddress.length() < 10) {
                    setAcceptable(addressResult, false, "Too short!");
                    return;
                }

                setAcceptable(addressResult, true, null);
                isAddressAcceptable = true;
            }
        });

        genderField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typedGender = genderField.getText() + e.getKeyChar();
                if (typedGender.isEmpty()) {
                    setAcceptable(genderResult, false, "Empty!");
                } else {
                    setAcceptable(genderResult, true, null);
                    isGenderAcceptable = true;
                }
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new WelcomeFrame();
        });

        signUpButton.addActionListener(event -> {
            if (isUsernameAcceptable && isPasswordAcceptable && isConfirmPasswordAcceptable && isAddressAcceptable && isEmailAcceptable && isGenderAcceptable) {
                instance.getLoginUsers().add(new LoginUser(UUID.randomUUID(), usernameField.getText(), String.valueOf(passwordField.getPassword()), addressField.getText(), emailField.getText(), genderField.getText()));
                JOptionPane.showMessageDialog(null, "Successfully registered!");
                dispose();
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(null, "Please fill out all fields correctly!");
            }
        });
    }

    public void setAcceptable(JLabel jTextField, boolean acceptable, @Nullable String reason) {
        if (acceptable) {
            jTextField.setBackground(new java.awt.Color(0, 255, 0));
            jTextField.setText("✅");
        } else {
            if (reason == null)
                throw new IllegalArgumentException("Reason cannot be null if the field is not acceptable!");

            jTextField.setBackground(new java.awt.Color(255, 0, 0));
            jTextField.setText("❌ " + reason);
        }
    }
}