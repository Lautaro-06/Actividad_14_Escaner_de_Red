package networkScanner;
import java.awt.Component;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ExploradorResultados {

    public static void exportarCSV(String rutaArchivo, List<ResultadoEquipo> resultados) {
        if (resultados == null || resultados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay datos para exportar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write("IP,Host,Estado,Latencia(ms)\n");
            for (ResultadoEquipo equipo : resultados) {
                writer.write(String.format("\"%s\",\"%s\",%s,%s\n",
                        equipo.getIp(),
                        equipo.getHost(),
                        equipo.getEstado(),
                        equipo.getTiempoRespuesta() > 0 ? equipo.getTiempoRespuesta() : "-"
                ));
            }
            JOptionPane.showMessageDialog(null, "Archivo guardado en:\n" + rutaArchivo, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar:\n" + e.getMessage() + "\nRuta probada: " + rutaArchivo, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void exportarDesdeGUI(Component parent, List<ResultadoEquipo> resultados) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar resultados");
        fileChooser.setSelectedFile(new File("scan_resultados.csv"));

        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            String ruta = fileChooser.getSelectedFile().getAbsolutePath();
            if (!ruta.endsWith(".csv")) ruta += ".csv";
            exportarCSV(ruta, resultados);
        }
    }
}
