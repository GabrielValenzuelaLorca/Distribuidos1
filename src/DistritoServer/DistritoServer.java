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
    static DatagramSocket socketuni;
    static DatagramPacket packet;


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
        socketuni = new DatagramSocket();
        // send request
        byte[] buf = new byte[256];
        String envio = "id";
        buf = envio.getBytes();
        InetAddress address = getByName("127.0.0.0");
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 9090);
        socketuni.send(packet);

        // get response
        buf = new byte[256];
        packet = new DatagramPacket(buf, buf.length);
        socketuni.receive(packet);
        String mensaje= new String(packet.getData());
        if ("None".equals(mensaje.trim())){
            System.out.println("Mira BRA te tiro un NONES");
        }
        id=Integer.parseInt(mensaje.trim());

        BufferedReader datos = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("AGREGAR TITAN");
        System.out.println(nombre + "Nombre Titan\n>");
        nombretitan = datos.readLine();
        System.out.println(nombre + "Tipo\n>");
        tipo = datos.readLine();
        Titan titan = new Titan(id,nombretitan,tipo);
        String msn="Aparecio un nuevo titan! "+titan.nombre+" "+titan.tipo+" "+titan.id;
        byte[] buffer;
        socket = new MulticastSocket(puerto_multi);
        socket.joinGroup(getByName(ip_multi));
        buffer = new byte [10000];
        buffer = msn.getBytes();
        DatagramPacket pack= new DatagramPacket(buffer,buffer.length, getByName(ip_multi),puerto_multi);
        socket.send(packet);
        socket.close();
        buffer = new byte [10000];
        return titan;
    }
    public static class menu implements Runnable{

        public void run(){
            try {
                String opcion;
                boolean flag=true;
                byte[] buf;

                while (flag) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println(nombre + " (1) AGREGAR TITAN");
                    System.out.println(nombre + " (2) SALIR\n>");
                    opcion = br.readLine();

                    if ("1".equals(opcion)) {
                        titan=agregar_titan();
                        titanes.add(titan);


                    } else if ("2".equals(opcion)) {
                        System.out.println("MIRA BRA QUE GÜEN SOUT");
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

    public static class recepcion_titanes implements Runnable{

        public void run(){
            try{
                while(true){
                    String entrada;
                    byte[] buf = new byte[1000];
                    socketuni = new DatagramSocket(puerto_recep);
                    packet = new DatagramPacket(buf, buf.length);
                    socketuni.receive(packet);
                    entrada = new String(packet.getData());
                    String[] entrada_lista=entrada.split(" ");
                    int id=Integer.parseInt(entrada_lista[1].trim());
                    for (Iterator<Titan> iter = titanes.listIterator(); iter.hasNext(); ) {
                        Titan a = iter.next();
                        if (a.id==id) {
                            iter.remove();
                        }
                    }
                    String envio="";
                    if (entrada_lista[0].trim()=="0"){
                        envio="Se capturo el titan: "+entrada_lista[2].trim();
                    } else if(entrada_lista[0].trim()=="1"){
                        envio="Se asesino el titan: "+entrada_lista[2].trim();
                    }
                    byte[] buffer;
                    socket = new MulticastSocket(puerto_multi);
                    socket.joinGroup(getByName(ip_multi));
                    buffer = new byte [10000];
                    buffer = envio.getBytes();
                    DatagramPacket packet= new DatagramPacket(buffer,buffer.length, getByName(ip_multi),puerto_multi);
                    socket.send(packet);
                    socket.close();
                    buffer = new byte [10000];
                }

                }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class titanes_periodicos implements Runnable{

        public void run(){
            try{
                while(true){
                    int i;
                    String envio="";
                    for(i=0;i<titanes.size();i++){

                        envio=envio+titanes.get(i).id+" "+titanes.get(i).nombre+" "+titanes.get(i).tipo+"-";
                    }
                    byte[] buf;
                    socket = new MulticastSocket(puerto_multi);
                    socket.joinGroup(getByName(ip_multi));
                    buf = new byte [10000];
                    buf = envio.getBytes();
                    DatagramPacket packet= new DatagramPacket(buf,buf.length, getByName(ip_multi),puerto_multi);
                    socket.send(packet);
                    socket.close();
                    buf = new byte [10000];
                    try{
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {


        try{
            Thread hebra_server = new Thread(new recepcion_titanes());
            Thread hebra_menu = new Thread(new menu());
            Thread hebra_actualizar = new Thread(new titanes_periodicos());
            hebra_server.start();
            hebra_actualizar.start();
            hebra_menu.start();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
