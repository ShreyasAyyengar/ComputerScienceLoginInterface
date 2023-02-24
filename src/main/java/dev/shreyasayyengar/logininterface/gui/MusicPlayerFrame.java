package dev.shreyasayyengar.logininterface.gui;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayerFrame extends JFrame {
    private JButton play1, play2, play3, stopButton;
    private JPanel mainPanel;
    public Clip currentClip;

    public MusicPlayerFrame() {
        setContentPane(mainPanel);
        setSize(650, 350);
        setTitle("Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        play1.addActionListener(event -> playMusic("src/main/resources/In Your Eyes.wav"));

        play2.addActionListener(event -> playMusic("src/main/resources/Blinding Lights.wav"));

        play3.addActionListener(event -> playMusic("src/main/resources/Save Your Tears.wav"));

        stopButton.addActionListener(event -> {
            if (currentClip != null) {
                currentClip.stop();
            }
        });
    }

    public void playMusic(String path) {
        try {
            if (currentClip != null) {
                currentClip.stop();
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            currentClip = clip;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}