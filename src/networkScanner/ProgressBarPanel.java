package networkScanner;

import javax.swing.*;
import java.awt.*;

public class ProgressBarPanel extends JPanel {
    private JProgressBar progressBar;

    public ProgressBarPanel() {
        setLayout(new BorderLayout());
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.CENTER);
    }

    // Método para actualizar progreso
    public void setProgress(int value) {
        SwingUtilities.invokeLater(() -> progressBar.setValue(value));
    }

    // Reinicia la barra a 0
    public void reset() {
        SwingUtilities.invokeLater(() -> progressBar.setValue(0));
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }
}
