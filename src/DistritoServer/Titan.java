package DistritoServer;

import java.io.Serializable;

public class Titan implements Serializable{
    int id;
    String nombre, tipo;
    public Titan( int id,String nombre, String tipo){

        this.id=id;
        this.nombre=nombre;
        this.tipo=tipo;
    }
}