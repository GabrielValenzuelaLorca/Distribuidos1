package DistritoServer;

import java.io.Serializable;

public class Titan implements Serializable{
    int id;
    String nombre, tipo;
    public Titan(String nombre, String tipo, int id){

        this.nombre=nombre;
        this.tipo=tipo;
        this.id=id;
    }
}