package dev.shreyasayyengar.logininterface.gui;

import javax.swing.*;

public class WelcomeFrame extends JFrame {
    private JPanel mainPanel;
    private JButton signInButton, signUpButton, exitButton;

    public WelcomeFrame() {
        setContentPane(mainPanel);
        setTitle("Welcome");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        signInButton.addActionListener(event -> {
            dispose();
            new LoginFrame();
        });

        signUpButton.addActionListener(event -> {
            dispose();
            new SignUpFrame();
        });

        exitButton.addActionListener(event -> System.exit(0));
    }
}