package networkScanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class NetworkUtils {
    private static final Pattern IP_PATTERN =
            Pattern.compile("^((25[0-5]|2[0-4]\\d|1?\\d?\\d)(\\.|$)){4}$");

    public static boolean validarIP(String ip) {
        return IP_PATTERN.matcher(ip).matches();
    }

    public static String obtenerHost(String ip) {
        try {
            Process process = Runtime.getRuntime().exec("nslookup " + ip);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (linea.contains("name =")) {
                        return linea.split("name =")[1].trim().replaceAll("\\.$", "");
                    }
                }
            }
        } catch (Exception ignored) {}
        return "Desconocido";
    }
}
