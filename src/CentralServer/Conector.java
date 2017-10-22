package CentralServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Conector {
    String serv="[SERVIDOR CENTRAL]";
    ServerSocket server;
    Socket socket;
    int puerto=9000;
    DataOutputStream salida;
    BufferedReader entrada;

    public void iniciar(){
        try{
            server = new ServerSocket(puerto);
            socket = new Socket();
            System.out.println("[Servidor Central] Esperando Conexion de Cliente...");
            socket = server.accept();
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String mensaje = entrada.readLine();
            System.out.println(mensaje);
            salida = new DataOutputStream(socket.getOutputStream());
            salida.writeUTF("Good Dog");

            entrada.close();
            salida.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void terminar(){
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

