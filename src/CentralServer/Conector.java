package CentralServer;

import java.io.*;
import java.net.*;
import java.util.List;

public class Conector {
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
                    System.out.println(serv+"No existe el distrito "+distrito);
                    socket.close();
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

