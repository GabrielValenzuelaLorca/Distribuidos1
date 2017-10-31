package DistritoServer;
import CentralServer.Conector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

import static java.net.InetAddress.getByName;

public class DistritoServer {
    static List<Titan> titanes = new ArrayList<Titan>();
    static String nombre, ip_multi, ip_recep, ip_sc;
    static int puerto_multi, puerto_recep, puerto_sc;
    static  Conector conector;
    static Titan titan;
    static MulticastSocket socket;

    public DistritoServer(String nombre, String ip_multi, int puerto_multi, String ip_recep, int puerto_recep, int puerto_sc, String ip_sc){
        this.ip_multi=ip_multi;
        this.ip_recep=ip_recep;
        this.nombre=nombre;
        this.puerto_multi=puerto_multi;
        this.puerto_recep=puerto_recep;
        this.puerto_sc=puerto_sc;
        this.ip_sc=ip_sc;

    }
    public static Titan agregar_titan() throws IOException {
        String nombretitan,tipo;
        int id;
        // get a datagram socket
        DatagramSocket socket = new DatagramSocket();
        // send request
        byte[] buf = new byte[256];
        String envio = "id";
        buf = envio.getBytes();
        InetAddress address = getByName(ip_sc);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 9090);
        socket.send(packet);

        // get response
        buf = new byte[256];
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String mensaje= new String(buf);
        id=Integer.parseInt(mensaje);

        BufferedReader datos = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("AGREGAR TITAN");
        System.out.println(nombre + "Nombre Titan\n>");
        nombretitan = datos.readLine();
        System.out.println(nombre + "Tipo\n>");
        tipo = datos.readLine();
        Titan titan = new Titan(id,nombretitan,tipo);
        datos.close();
        return titan;
    }
    public static class menu implements Runnable{

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
                        titan=agregar_titan();
                        titanes.add(titan);
                        String envio=(titan.id+" "+titan.nombre+" "+titan.tipo);
                        socket = new MulticastSocket(puerto_multi);
                        socket.joinGroup(getByName(ip_multi));
                        byte[] buf = new byte [1000];
                        buf = envio.getBytes();
                        DatagramPacket packet= new DatagramPacket(buf,buf.length, getByName(ip_multi),puerto_multi);
                        socket.send(packet);
                        socket.close();
                        buf = new byte [1000];
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
