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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIp_multi() {
        return ip_multi;
    }

    public void setIp_multi(String ip_multi) {
        this.ip_multi = ip_multi;
    }

    public String getIp_recep() {
        return ip_recep;
    }

    public void setIp_recep(String ip_recep) {
        this.ip_recep = ip_recep;
    }

    public int getPuerto_multi() {
        return puerto_multi;
    }

    public void setPuerto_multi(int puerto_multi) {
        this.puerto_multi = puerto_multi;
    }

    public int getPuerto_recep() {
        return puerto_recep;
    }

    public void setPuerto_recep(int puerto_recep) {
        this.puerto_recep = puerto_recep;
    }
}
