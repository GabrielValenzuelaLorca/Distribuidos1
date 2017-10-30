package CentralServer;

import java.net.InetAddress;

public class Cliente {
    InetAddress ip;
    String distrito;

    public Cliente(InetAddress ip, String distrito){
        this.ip=ip;
        this.distrito=distrito;
    }

    public void cambiar_distrito(String distrito){
        this.distrito=distrito;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
}
