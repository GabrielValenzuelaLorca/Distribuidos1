package CentralServer;

import java.io.*;
import java.net.*;

public class Conector {
    String serv="[SERVIDOR CENTRAL] ";
    DatagramSocket socket;
    byte[] buf = new byte[256];
    DatagramPacket packet;
    int puerto;


    public Conector(String tipo) throws IOException {
        if ("Cliente".equals(tipo)){
            puerto = 9000;
        }
        else if ("Distrito".equals(tipo)){
            //puerto igual a pene
        }
    }


    public Cliente leerCliente() throws IOException {
        String distrito, entrada;
        InetAddress ip;
        Cliente cliente;

        System.out.println(serv + "Esperando Conexion de Cliente...");

        socket = new DatagramSocket(puerto);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        entrada = new String(packet.getData());

        ip = packet.getAddress();
        int port = packet.getPort();
        distrito=entrada.trim();
        //ENVIO DE PAQUETE DE RESPUESTA
        /*packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        */
        socket.close();

        System.out.println("ip: "+ip+" distrito: "+distrito);
        cliente = new Cliente(ip,distrito);

        return cliente;
    }



}

