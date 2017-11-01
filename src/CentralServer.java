import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static java.net.InetAddress.getByName;

class Cliente {
    InetAddress ip;
    String distrito;

    public Cliente(InetAddress ip, String distrito){
        this.ip=ip;
        this.distrito=distrito;
    }

    public void cambiar_distrito(String distrito){
        this.distrito=distrito;
    }

    public InetAddress getIp() {
        return ip;
    }

    public String getDistrito() {
        return distrito;
    }

}

class Distrito {
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


class Conector {
    String serv="[SERVIDOR CENTRAL] ";
    DatagramSocket socket;
    byte[] buf;
    DatagramPacket packet;
    int puerto;


    public Conector(String tipo) throws IOException {
        if ("Cliente".equals(tipo)){
            puerto = 9000;
        }
        else if ("Distrito".equals(tipo)){
            puerto = 9090;
        }
    }

    public Cliente leerCliente(List<Distrito> distritos) throws IOException {
        String distrito, entrada,opcion,envio;
        InetAddress ip;
        Cliente cliente;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Distrito distrito_de_lista = null;

        System.out.println(serv + "Esperando Conexion de Cliente...");

        buf = new byte[256];
        socket = new DatagramSocket(puerto);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        entrada = new String(packet.getData());

        ip = packet.getAddress();
        int port = packet.getPort();
        distrito=entrada.trim();

        System.out.println(serv+"Dar autorizacion a "+ip+" por Distrito "+distrito+"?");
        while (true) {
            System.out.println("1.- SI\n2.- NO\n>");
            opcion = br.readLine();

            if ("1".equals(opcion)) {
                System.out.println(serv+"Respuesta a "+ip+" por "+distrito);
                for (int i=0;i<distritos.size();i++){
                    if(distritos.get(i).getNombre().equals(distrito)){
                        distrito_de_lista=distritos.get(i);
                    }
                }
                if (distrito_de_lista!=null){
                    System.out.println(serv+"Nombre: "+distrito_de_lista.getNombre()+", IP Multicast: "+distrito_de_lista.getIp_multi()+", Puerto Multicast: "+distrito_de_lista.getPuerto_multi()+",\nIP Peticiones: "+distrito_de_lista.getIp_recep()+", Puerto Peticiones: "+distrito_de_lista.getPuerto_recep());
                    buf= new byte[256];
                    envio=distrito_de_lista.getNombre()+" "+distrito_de_lista.getIp_multi().toString()+" "+distrito_de_lista.getPuerto_multi()+" "+distrito_de_lista.getIp_recep().toString()+" "+distrito_de_lista.getPuerto_recep();
                    buf=envio.getBytes();
                    packet = new DatagramPacket(buf, buf.length, ip, port);
                    socket.send(packet);
                    socket.close();
                    cliente = new Cliente(ip,distrito);
                    return cliente;

                }else{
                    System.out.println(serv+"No existe el distrito "+distrito+". La conexion fue rechazada");
                    buf= new byte[256];
                    buf="Rechazada".getBytes();
                    packet = new DatagramPacket(buf, buf.length, ip, port);
                    socket.send(packet);
                    return null;
                }

            } else if ("2".equals(opcion)) {
                System.out.println(serv+"Respuesta a "+ip+" por "+distrito);
                System.out.println(serv+"Rechazada");
                buf= new byte[256];
                buf="Rechazada".getBytes();
                packet = new DatagramPacket(buf, buf.length, ip, port);
                socket.send(packet);
                socket.close();
                return null;

            } else {
                System.out.println(serv+"Ingrese una opcion valida");
            }
        }


    }

    public int leerId(int id) throws IOException {
        InetAddress ip;
        String envio,entrada;

        buf = new byte[256];
        socket = new DatagramSocket(puerto);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        entrada = new String(packet.getData());

        ip = packet.getAddress();
        int port = packet.getPort();

        if ("id".equals(entrada.trim())){
            buf= new byte[256];
            envio= String.valueOf(id+1);
            buf=envio.getBytes();
            packet = new DatagramPacket(buf, buf.length, ip, port);
            socket.send(packet);
            socket.close();
            return id+1;
        }else{
            System.out.println("Entre al else");
            System.out.println(entrada.trim());
            buf= new byte[256];
            buf="None".getBytes();
            packet = new DatagramPacket(buf, buf.length, ip, port);
            socket.send(packet);
            socket.close();
            return id;
        }

    }

}

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
                    boolean flagg=true;
                    for (int i=0;i<clientes.size();i++){
                        if (cliente.equals(clientes.get(i))){
                            clientes.get(i).cambiar_distrito(cliente.getDistrito());
                            flagg=false;
                        }
                    }
                    if (flagg){
                        clientes.add(cliente);
                    }

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