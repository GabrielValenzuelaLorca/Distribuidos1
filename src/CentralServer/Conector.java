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
        String opcion;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean flag=true;

        System.out.println(serv + "Esperando Conexion de Cliente...");

        socket = new DatagramSocket(puerto);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        entrada = new String(packet.getData());

        ip = packet.getAddress();
        int port = packet.getPort();
        distrito=entrada.trim();

        System.out.println(serv+"Dar autorizacion a "+ip+" por Distrito "+distrito+"?");
        while (flag) {
            System.out.println("1.- SI\n2.- NO\n>");
            opcion = br.readLine();

            if ("1".equals(opcion)) {
                flag=false;
                System.out.println(serv+"Respuesta a "+ip+" por "+distrito);
                //FALTA ENVIAR DATOS DEL DISTRITO

            } else if ("2".equals(opcion)) {
                System.out.println(serv+"Respuesta a "+ip+" por "+distrito);
                System.out.println(serv+"Rechazada");
                //FALTA VACIAR EL BUF ANTES DE ENVIAR LA RESPUESTA
                buf="None".getBytes();
                packet = new DatagramPacket(buf, buf.length, ip, port);
                socket.send(packet);
                flag=false;
                distrito="0";

            } else {
                System.out.println(serv+"Ingrese una opcion valida");
            }
        }

        socket.close();
        cliente = new Cliente(ip,distrito);

        return cliente;
    }



}

