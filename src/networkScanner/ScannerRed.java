package networkScanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ScannerRed {

    public static List<ResultadoEquipo> escanearRango(String inicio, String fin, int timeout) {
        List<ResultadoEquipo> resultados = new ArrayList<>();

        try {
            String[] inicioParts = inicio.split("\\.");
            String[] finParts = fin.split("\\.");

            // Solo funciona si las primeras tres partes son iguales
            if (inicioParts.length != 4 || finParts.length != 4) {
                throw new IllegalArgumentException("Formato de IP inválido");
            }
            if (!inicioParts[0].equals(finParts[0]) ||
                !inicioParts[1].equals(finParts[1]) ||
                !inicioParts[2].equals(finParts[2])) {
                throw new IllegalArgumentException("Rango solo permitido dentro de la misma subred");
            }

            int start = Integer.parseInt(inicioParts[3]);
            int end = Integer.parseInt(finParts[3]);
            String baseIP = inicioParts[0] + "." + inicioParts[1] + "." + inicioParts[2] + ".";

            for (int i = start; i <= end; i++) {
                String ip = baseIP + i;
                boolean activo = false;
                long tiempo = -1;
                String host = "Desconocido";

                try {
                    long startPing = System.currentTimeMillis();
                    InetAddress inet = InetAddress.getByName(ip);
                    activo = inet.isReachable(timeout);
                    tiempo = System.currentTimeMillis() - startPing;

                    if (activo) {
                        try {
                            host = inet.getHostName();
                        } catch (Exception e) {
                            host = "Desconocido";
                        }
                    }
                } catch (Exception e) {
                    activo = false;
                }

                resultados.add(new ResultadoEquipo(
                        ip,
                        host,
                        activo ? "ACTIVO" : "INACTIVO",
                        activo ? tiempo : -1
                ));
            }

        } catch (Exception e) {
            System.err.println("Error al escanear rango: " + e.getMessage());
        }

        return resultados;
    }
}
