package CentralServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CentralServer {
    static List<Distrito> distritos = new ArrayList<Distrito>();
    static Conector conector;
    static String serv="[SERVIDOR CENTRAL] ";
    static List<Cliente> clientes = new ArrayList<Cliente>();

    public static class menu implements Runnable{

        public static Distrito agregar_distrito() throws IOException {
            String nombre,ip_multi,ip_recep;
            int puerto_multi, puerto_recep;
            Distrito distrito;

            BufferedReader datos = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("AGREGAR DISTRITO");
            System.out.println(serv + "Nombre Distrito\n>");
            nombre = datos.readLine();
            System.out.println(serv + "IP Multicast\n>");
            ip_multi = datos.readLine();
            System.out.println(serv + "Puerto Multicast\n>");
            puerto_multi = Integer.valueOf(datos.readLine().trim());
            System.out.println(serv + "IP Peticiones\n>");
            ip_recep = datos.readLine();
            System.out.println(serv + "Puerto Peticiones\n>");
            puerto_recep = Integer.valueOf(datos.readLine().trim());

            distrito = new Distrito(nombre,ip_multi,puerto_multi,ip_recep,puerto_recep);
            datos.close();
            return distrito;

        }

        public void run(){
            try {
                String opcion;
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                boolean flag=true;
                while (flag) {
                    System.out.println(serv + " (1) AGREGAR DISTRITO");
                    System.out.println(serv + " (2) SALIR\n>");
                    opcion = br.readLine();
                    if ("1".equals(opcion)) {
                        distritos.add(agregar_distrito());
                    } else if ("2".equals(opcion)) {
                        br.close();
                        flag=false;
                        //Falta cerrar cada cosa
                    } else {
                        System.out.println("Ingrese una opcion valida");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class servidor implements Runnable{

        public void run(){
            conector = new Conector();
            conector.iniciar();
        }
    }

    public static void main(String[] args) {

        try{
            boolean flag=true;
            Thread hebra_server = new Thread(new servidor());
            Thread hebra_menu = new Thread(new menu());
            hebra_server.start();
            hebra_menu.start();
            while (flag){
                if (!hebra_menu.isAlive()){
                    hebra_server.interrupt();
                    System.out.println("Me salí perro");
                    flag=false;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        //c = new Conector();
        //c.iniciar();
    }

}