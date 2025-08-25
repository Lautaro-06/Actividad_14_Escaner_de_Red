package networkScanner;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainVentana ventana = new MainVentana();
            ventana.setVisible(true);
        });
    }
}
