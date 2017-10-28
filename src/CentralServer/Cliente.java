package CentralServer;

public class Cliente {
    String ip;
    Distrito distrito;

    public void Cliente(String ip,Distrito distrito){
        this.ip=ip;
        this.distrito=distrito;
    }

    public void cambiar_distrito(Distrito distrito){
        this.distrito=distrito;
    }
}
