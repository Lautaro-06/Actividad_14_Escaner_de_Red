package networkScanner;

public class ResultadoEquipo {
    private final String ip;
    private final String host;
    private final String estado;
    private final long tiempoRespuesta;

    public ResultadoEquipo(String ip, String host, String estado, long tiempoRespuesta) {
        this.ip = ip;
        this.host = host;
        this.estado = estado;
        this.tiempoRespuesta = tiempoRespuesta;
    }

    public String getIp() { return ip; }
    public String getHost() { return host; }
    public String getEstado() { return estado; }
    public long getTiempoRespuesta() { return tiempoRespuesta; }
}
