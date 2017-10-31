package CentralServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class CentralServer {
    static List<Distrito> distritos = new ArrayList<Distrito>();
    static Conector conector, conector_id;
    static String serv="[SERVIDOR CENTRAL] ";
    static List<Cliente> clientes = new ArrayList<Cliente>();
    static int id=0;

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
            return distrito;

        }

        public void run(){
            try {
                String opcion;
                boolean flag=true;

                while (flag) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println(serv + "(1) AGREGAR DISTRITO");
                    System.out.println(serv+"(2) TERMINAR\n>");
                    opcion = br.readLine();
                    if ("1".equals(opcion)) {
                        distritos.add(agregar_distrito());
                    }
                    else if ("2".equals(opcion)){
                        System.out.println(serv+"Fin de agregar distritos");
                        flag=false;
                    } else {
                        System.out.println(serv+"Ingrese una opcion valida");
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class servidor implements Runnable{
        Cliente cliente;
        public void run(){
            try {
                conector = new Conector("Cliente");

                while (true) {
                    cliente = conector.leerCliente(distritos);
                    /*if (cliente not in clientes){
                        clientes.add(cliente);
                    }else{
                        for (int i=0;i<clientes.size()<i++){
                            if (cliente.equals(clientes.get(i))){
                                clientes.get(i).cambiar_distrito(cliente.getDistrito);
                            }
                        }
                    }
                    Ver si cliente esta en la lista*/
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class id_titanes implements Runnable{
        int id_temp;
        public void run() {
            try {
                conector_id = new Conector("Distrito");
                while (true) {
                    id_temp=conector_id.leerId(id);
                    id=id_temp;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        try{
            Thread hebra_server = new Thread(new servidor());
            Thread hebra_menu = new Thread(new menu());
            Thread hebra_id = new Thread((new id_titanes()));
            hebra_menu.start();
            while (hebra_menu.isAlive()){
                continue;
            }
            hebra_server.start();
            hebra_id.start();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}