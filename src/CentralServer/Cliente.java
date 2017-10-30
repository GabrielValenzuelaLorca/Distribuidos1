package CentralServer;

public class Cliente {
    String ip;
    String distrito;

    public Cliente(String ip,String distrito){
        this.ip=ip;
        this.distrito=distrito;
    }

    public void cambiar_distrito(String distrito){
        this.distrito=distrito;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
}
