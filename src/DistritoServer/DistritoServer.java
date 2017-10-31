package DistritoServer;
import CentralServer.Conector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class DistritoServer {
    static List<Titan> titanes = new ArrayList<Titan>();
    static String nombre, ip_multi, ip_recep;
    static int puerto_multi, puerto_recep;
    static  Conector conector;
    static Multicast multicast;

    public DistritoServer(String nombre, String ip_multi, int puerto_multi, String ip_recep, int puerto_recep){
        this.ip_multi=ip_multi;
        this.ip_recep=ip_recep;
        this.nombre=nombre;
        this.puerto_multi=puerto_multi;
        this.puerto_recep=puerto_recep;

    }
    public static class menu implements Runnable{

        public static Titan agregar_titan() throws IOException {
            String nombretitan,tipo;
            int id;
            BufferedReader datos = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("AGREGAR TITAN");
            System.out.println(nombre + "Nombre Titan\n>");
            nombretitan = datos.readLine();
            System.out.println(nombre + "Tipo\n>");
            tipo = datos.readLine();
            Titan titan = new Titan(nombretitan,tipo,1);
            datos.close();
            return titan;
        }

        public void run(){
            try {
                String opcion;
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                boolean flag=true;
                while (flag) {
                    System.out.println(nombre + " (1) AGREGAR TITAN");
                    System.out.println(nombre + " (2) SALIR\n>");
                    opcion = br.readLine();
                    if ("1".equals(opcion)) {
                        titanes.add(agregar_titan());
                        multicast.send(nombre +": Aparecio un nuevo titan!");
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
            //no saque esta wea de tu Central Server porque no se pa que wea era :3
            conector = new Conector();
            conector.iniciar();


        }
    }


    public static void main(String[] args) {


        try{
            multicast= new Multicast(puerto_multi,ip_multi);
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

    }

}
