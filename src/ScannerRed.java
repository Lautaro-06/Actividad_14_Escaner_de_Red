import java.io.IOException;
import java.net.InetAddress;

public class ScannerRed {
    public static void main(String[] args) {
        String ipBase = "192.168.0.";
        int inicio = 1;
        int fin = 10;

        for (int i = inicio; i <= fin; i++) {
            String ip = ipBase + i;
            try {
                InetAddress inet = InetAddress.getByName(ip);
                boolean reachable = inet.isReachable(1000);
                System.out.println(ip + " está " + (reachable ? "ACTIVO" : "INACTIVO"));
            } catch (IOException e) {
                System.out.println("Error al hacer ping a: " + ip);
            }
        }
    }
}
