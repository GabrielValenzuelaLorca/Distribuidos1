package Paquete;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.net.InetAddress.getByName;

public class Distrito {
    String nombre;
    int puerto_multi, puerto_recep;
    InetAddress ip_multi,ip_recep;

    public Distrito(String nombre, String ip_multi, int puerto_multi, String ip_recep, int puerto_recep){
        try {
            this.ip_multi=getByName(ip_multi);
            this.ip_recep=getByName(ip_recep);
            this.nombre=nombre;
            this.puerto_multi=puerto_multi;
            this.puerto_recep=puerto_recep;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public InetAddress getIp_multi() {
        return ip_multi;
    }

    public InetAddress getIp_recep() {
        return ip_recep;
    }

    public int getPuerto_multi() {
        return puerto_multi;
    }

    public int getPuerto_recep() {
        return puerto_recep;
    }

}
