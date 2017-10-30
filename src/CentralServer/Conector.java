package CentralServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Conector {
    String serv="[SERVIDOR CENTRAL] ";
    ServerSocket server;
    Socket socket;
    int puerto;
    BufferedReader entrada;

    public Conector(String tipo) throws IOException {
        if ("Cliente".equals(tipo)){
            puerto = 9000;
        }
        else if ("Distrito".equals(tipo)){
            //puerto igual a pene
        }
        server = new ServerSocket(puerto);
    }

    public Cliente leerCliente() throws IOException {
        String ip,distrito;
        Cliente cliente;

        System.out.println(serv + "Esperando Conexion de Cliente...");
        socket = server.accept();

        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ip = String.valueOf(socket.getRemoteSocketAddress());
        distrito = entrada.readLine().trim();

        entrada.close();
        socket.close();
        System.out.println("ip: "+ip+" distrito: "+distrito);
        cliente = new Cliente(ip,distrito);

        return cliente;
    }



}

