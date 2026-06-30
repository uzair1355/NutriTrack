package nutritrack;

import javax.swing.*;

/**
 * NutriTrack — Gym Nutrition Calculator
 * Entry point: sets the Look & Feel and launches the main window.
 *
 * To compile:  javac -d out src/nutritrack/*.java
 * To run:      java -cp out nutritrack.Main
 */
public class Main {
    public static void main(String[] args) {
        // Use system L&F for native decorations, else fall back to default
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        // All Swing work must happen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
