package dev.shreyasayyengar.logininterface.gui;

import dev.shreyasayyengar.logininterface.objects.LoginUser;

import javax.swing.*;

public class ReservationFrame extends JFrame {
    private JPanel mainPanel;
    private JComboBox movieSelection;
    private JButton viewReservations, exitButton, reserve, clearReservations;

    public ReservationFrame(LoginUser user) {
        setContentPane(mainPanel);
        setSize(650, 350);
        setTitle("Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        exitButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        reserve.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Reservation Successful! Enjoy your movie!");

            user.reservations().add(movieSelection.getSelectedItem().toString());
        });

        viewReservations.addActionListener(e -> {
            if (user.reservations().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You have no reservations!");
            } else {
                StringBuilder toShow = new StringBuilder("Your reservations are: ");
                for (String reservation : user.reservations()) {
                    toShow.append("\n").append(reservation).append(", ");
                }

                JOptionPane.showMessageDialog(null, toShow.toString());
            }
        });

        clearReservations.addActionListener(e -> {
            user.reservations().clear();
            JOptionPane.showMessageDialog(null, "Your reservations have been cleared!");
        });
    }
}