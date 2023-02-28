package dev.shreyasayyengar.logininterface.gui;

import dev.shreyasayyengar.logininterface.LoginProgram;
import dev.shreyasayyengar.logininterface.objects.GmailServiceSender;
import dev.shreyasayyengar.logininterface.objects.LoginUser;
import dev.shreyasayyengar.logininterface.util.Util;

import javax.swing.*;
import java.util.Optional;

public class ForgotFrame extends JFrame {
    private JPanel mainPanel;
    private JButton retrieveAccountButton;
    private JTextField emailField;

    public ForgotFrame() {
        setContentPane(mainPanel);
        setSize(650, 350);
        setTitle("Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        retrieveAccountButton.addActionListener(e -> {

            String typedEmail = emailField.getText();

            if (typedEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your email address.");
                return;
            }

            if (!Util.validate(typedEmail)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
                return;
            }

            Optional<LoginUser> optionalUser = LoginProgram.getInstance().getLoginUsers().stream().filter(loginUser -> loginUser.email().equalsIgnoreCase(typedEmail)).findFirst();
            optionalUser.ifPresentOrElse(email -> {
                new GmailServiceSender(optionalUser.get());

                JOptionPane.showMessageDialog(this, "Your account has been retrieved. Please check your email for further instructions.");
                dispose();
                new LoginFrame();

            }, () -> JOptionPane.showMessageDialog(this, "No account was found with that email address."));
        });
    }
}