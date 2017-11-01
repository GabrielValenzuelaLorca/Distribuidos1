package DistritoServer;

import java.io.IOException;
import java.net.*;

public class Multicast {
    static int port;
    static String address;
    MulticastSocket socket;

    public Multicast(int port,String address){

        this.port=port;
        this.address=address;

    }
    public void send(String envio){
        try{
            socket = new MulticastSocket(port);
            socket.joinGroup(InetAddress.getByName(address));
            byte[] buf = new byte [1000];
            buf = envio.getBytes();
            DatagramPacket packet= new DatagramPacket(buf,buf.length,InetAddress.getByName(address),port);
            socket.send(packet);
            socket.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public String recibe() throws IOException{

        socket = new MulticastSocket(port);
        socket.joinGroup(InetAddress.getByName(address));
        byte[] buffer = new byte[1000];
        DatagramPacket packet= new DatagramPacket(buffer,buffer.length);
        socket.receive(packet);
        socket.close();
        String mensaje= new String(buffer);
        return mensaje;

    }



}