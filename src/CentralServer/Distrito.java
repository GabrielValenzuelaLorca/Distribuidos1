package CentralServer;

public class Distrito {
    String nombre, ip_multi, ip_recep;
    int puerto_multi, puerto_recep;

    public Distrito(String nombre, String ip_multi, int puerto_multi, String ip_recep, int puerto_recep){
        this.ip_multi=ip_multi;
        this.ip_recep=ip_recep;
        this.nombre=nombre;
        this.puerto_multi=puerto_multi;
        this.puerto_recep=puerto_recep;
    }
}
