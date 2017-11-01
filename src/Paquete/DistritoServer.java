package Paquete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import static java.net.InetAddress.getByName;

public class DistritoServer {
    static List<Titan> titanes = new ArrayList<Titan>();
    static String nombre, ip_multi, ip_recep, ip_sc, dist="[Distrito";
    static int puerto_multi, puerto_recep;
    static boolean tamano=false;

    public static Titan agregar_titan() throws IOException {
        String nombretitan,opcion,tipo="";
        int id;
        boolean flag=true;
        MulticastSocket socket;
        DatagramSocket socketuni;
        DatagramPacket packet;
        byte[] buf;

        BufferedReader datos = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("PUBLICAR TITAN");
        System.out.println(dist + "Introducir nombre\n>");
        nombretitan = datos.readLine().trim();
        while (flag) {
            System.out.println(dist + "Introducir tipo\n1.- Normal\n2.- Excentrico\n3.- Cambiante\n>");
            opcion = datos.readLine().trim();
            if ("1".equals(opcion)) {
                tipo = "Normal";
                flag=false;
            } else if ("2".equals(opcion)) {
                tipo = "Excentrico";
                flag=false;
            } else if ("3".equals(opcion)) {
                tipo = "Cambiante";
                flag=false;
            } else{
                System.out.println("Ingrese un valor valido");
            }
        }

        socketuni = new DatagramSocket();
        buf = new byte[256];
        String envio = "id";
        buf = envio.getBytes();
        InetAddress address = getByName(ip_sc);
        packet = new DatagramPacket(buf, buf.length, address, 9090);
        socketuni.send(packet);

        // get response
        buf = new byte[256];
        packet = new DatagramPacket(buf, buf.length);
        socketuni.receive(packet);
        String mensaje= new String(packet.getData());
        id=Integer.parseInt(mensaje.trim());


        Titan titan = new Titan(id,nombretitan,tipo);
        System.out.println("Se ha publicado el titan: "+titan.nombre+"\n**********\nID: "+titan.id+"\nNombre:  "+titan.nombre+"\nTipo: "+titan.tipo+"\n**********");
        String msn="Aparece nuevo Titan! "+titan.nombre+", tipo "+titan.tipo+", ID "+titan.id+".";
        byte[] buffer;
        socket = new MulticastSocket(puerto_multi);
        socket.joinGroup(getByName(ip_multi));
        buffer = new byte [10000];
        buffer = msn.getBytes();
        DatagramPacket pack= new DatagramPacket(buffer,buffer.length, getByName(ip_multi),puerto_multi);
        socket.send(pack);
        socket.close();
        buffer = new byte [10000];
        return titan;
    }

    public static class menu implements Runnable{

        public void run(){
            try {
                String opcion;
                boolean flag=true;
                Titan titan;

                while (flag) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println(dist + " (1) AGREGAR TITAN");
                    opcion = br.readLine();

                    if ("1".equals(opcion)) {
                        titan=agregar_titan();
                        titanes.add(titan);
                        tamano=true;

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
        MulticastSocket socket;
        DatagramSocket socketuni;
        DatagramPacket packet;
        byte[] buf;
        String entrada;
        public void run(){
            try{
                while(true){
                    buf = new byte[1000];
                    socketuni = new DatagramSocket(puerto_recep);
                    packet = new DatagramPacket(buf, buf.length);
                    socketuni.receive(packet);
                    socketuni.close();
                    entrada = new String(packet.getData());
                    String[] entrada_lista=entrada.split(" ");
                    int id = Integer.parseInt(entrada_lista[1].trim());
                    for (Iterator<Titan> iter = titanes.listIterator(); iter.hasNext(); ) {
                        Titan a = iter.next();
                        if (a.id==id) {
                            iter.remove();
                        }
                    }
                    String envio="";
                    if ("0".equals(entrada_lista[0].trim())){
                        envio="Se capturo el titan: "+entrada_lista[2].trim();
                    } else if("1".equals(entrada_lista[0].trim())){
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
        MulticastSocket socket;
        DatagramPacket packet;
        byte[] buf;
        public void run(){
            try{
                while(true) {

                    int i;
                    String envio = "%";
                    for (i = 0; i < titanes.size(); i++) {

                        envio = envio + titanes.get(i).id + " " + titanes.get(i).nombre + " " + titanes.get(i).tipo + "-";
                    }
                    socket = new MulticastSocket(puerto_multi);
                    socket.joinGroup(getByName(ip_multi));
                    buf = new byte[10000];
                    buf = envio.getBytes();
                    packet = new DatagramPacket(buf, buf.length, getByName(ip_multi), puerto_multi);
                    socket.send(packet);
                    socket.close();
                    buf = new byte[10000];
                    Thread.sleep(3000);

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
    public static void main(String[] args) {

        BufferedReader datos = new BufferedReader(new InputStreamReader(System.in));

        try{
            System.out.println(dist + "] Nombre Servidor\n>");
            nombre = datos.readLine();
            dist=dist+" "+nombre+"] ";
            System.out.println(dist + "IP Multicast\n>");
            ip_multi = datos.readLine();
            System.out.println(dist + "Puerto Multicast\n>");
            puerto_multi = Integer.valueOf(datos.readLine().trim());
            System.out.println(dist + "IP Peticiones\n>");
            ip_recep = datos.readLine();
            System.out.println(dist + "Puerto Peticiones\n>");
            puerto_recep = Integer.valueOf(datos.readLine().trim());
            System.out.println(dist + "Ip Servidor Central\n>");
            ip_sc = datos.readLine().trim();


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
