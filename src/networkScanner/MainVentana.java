package networkScanner;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MainVentana extends JFrame {
    private DefaultTableModel model;
    private JButton btnScanear, btnGuardar, btnLimpiar, btnTutorial;
    private JTextField txtInicio, txtFin, txtTimeout;
    private JLabel lblContador;
    private ProgressBarPanel progressBarPanel;

    public MainVentana() {
        setTitle("Network Scanner Pro");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel superior con configuración y contador
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));

        JPanel panelConfig = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelConfig.add(new JLabel("IP Inicio:"));
        txtInicio = new JTextField("192.168.1.1", 12);
        panelConfig.add(txtInicio);

        panelConfig.add(new JLabel("IP Fin:"));
        txtFin = new JTextField("192.168.1.50", 12);
        panelConfig.add(txtFin);

        panelConfig.add(new JLabel("Timeout (ms):"));
        txtTimeout = new JTextField("5000", 5);
        panelConfig.add(txtTimeout);

        lblContador = new JLabel("Dispositivos: 0 activos | 0 totales");
        panelSuperior.add(panelConfig);
        panelSuperior.add(lblContador);

        // Tabla de resultados
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"IP", "Host", "Estado", "Latencia (ms)"});
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);

        // Barra de progreso usando ProgressBarPanel
        progressBarPanel = new ProgressBarPanel();

        // Botones
        btnScanear = new JButton("Escanear");
        btnScanear.addActionListener(this::iniciarEscaneo);

        btnGuardar = new JButton("Guardar CSV");
        btnGuardar.addActionListener(e -> guardarResultados());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarResultados());

        btnTutorial = new JButton("Tutorial");
        btnTutorial.addActionListener(e -> mostrarTutorial());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnScanear);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnTutorial);


        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(progressBarPanel, BorderLayout.SOUTH);
        add(panelBotones, BorderLayout.PAGE_END);
    }

    private void iniciarEscaneo(ActionEvent e) {
        if (!validarCampos()) return;

        btnScanear.setEnabled(false);
        model.setRowCount(0);

        SwingWorker<Void, ResultadoEquipo> worker = new SwingWorker<>() {
            private int activos = 0;
            private int totales = 0;

            @Override
            protected Void doInBackground() {
                List<ResultadoEquipo> resultados = ScannerRed.escanearRango(
                        txtInicio.getText(),
                        txtFin.getText(),
                        Integer.parseInt(txtTimeout.getText())
                );

                int total = resultados.size();
                for (int i = 0; i < total; i++) {
                    ResultadoEquipo r = resultados.get(i);
                    if ("ACTIVO".equals(r.getEstado())) activos++;
                    totales++;
                    publish(r);
                    int progreso = (int) ((i + 1) / (double) total * 100);
                    progressBarPanel.setProgress(progreso);
                }
                return null;
            }

            @Override
            protected void process(List<ResultadoEquipo> chunks) {
                for (ResultadoEquipo r : chunks) {
                    model.addRow(new Object[]{
                            r.getIp(),
                            r.getHost(),
                            r.getEstado(),
                            r.getTiempoRespuesta() > 0 ? r.getTiempoRespuesta() + " ms" : "-"
                    });
                }
                lblContador.setText(String.format("Dispositivos: %d activos | %d totales", activos, totales));
            }

            @Override
            protected void done() {
                btnScanear.setEnabled(true);
                progressBarPanel.reset();
            }
        };
        worker.execute();
    }

    private void guardarResultados() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay resultados para exportar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<ResultadoEquipo> resultados = new java.util.ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            resultados.add(new ResultadoEquipo(
                    (String) model.getValueAt(i, 0),
                    (String) model.getValueAt(i, 1),
                    (String) model.getValueAt(i, 2),
                    model.getValueAt(i, 3) instanceof String ?
                            -1 : Long.parseLong(model.getValueAt(i, 3).toString())
            ));
        }

        ExploradorResultados.exportarDesdeGUI(this, resultados);
    }

    private void limpiarResultados() {
        model.setRowCount(0);
        progressBarPanel.reset();
        lblContador.setText("Dispositivos: 0 activos | 0 totales");
    }

    private boolean validarCampos() {
        try {
            if (!NetworkUtils.validarIP(txtInicio.getText()) ||
                    !NetworkUtils.validarIP(txtFin.getText())) {
                throw new IllegalArgumentException("Formato de IP inválido");
            }
            Integer.parseInt(txtTimeout.getText());
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void mostrarTutorial() {
        try {
            Path path = java.nio.file.Paths.get("resources/tutorial.txt");
            String contenido = Files.readString(path, StandardCharsets.UTF_8);

            JTextArea area = new JTextArea(contenido);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this,
                    new JScrollPane(area),
                    "Tutorial",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar el tutorial\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainVentana().setVisible(true));
    }
}
