package CentralServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Distrito agregar_distrito() throws IOException {
        String nombre,ip_multi,ip_recep,serv="[SERVIDOR CENTRAL]";
        int puerto_multi, puerto_recep;
        Distrito distrito;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("AGREGAR DISTRITO");
        System.out.println(serv + "Nombre Distrito\n>");
        nombre = br.readLine();
        System.out.println(serv + "IP Multicast\n>");
        ip_multi = br.readLine();
        System.out.println(serv + "Puerto Multicast\n>");
        puerto_multi = Integer.valueOf(br.readLine().trim());
        System.out.println(serv + "IP Peticiones\n>");
        ip_recep = br.readLine();
        System.out.println(serv + "Puerto Peticiones\n>");
        puerto_recep = Integer.valueOf(br.readLine().trim());
        br.close();

        distrito = new Distrito(nombre,ip_multi,puerto_multi,ip_recep,puerto_recep);
        return distrito;
    }

    public static void main(String[] args) {
        List<Distrito> distritos = new ArrayList<Distrito>();
        System.out.println("[Servidor Central] Esperando Conexion de Cliente...");
        System.out.println("1.- AGREGAR DISTRITO");
        try{
            distritos.add(agregar_distrito());
        }catch (Exception e){

        }
        //Conector c = new Conector();
        //c.iniciar();
    }

}