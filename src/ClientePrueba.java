import java.io.IOException;
import java.net.*;

import static java.net.InetAddress.getByName;

public class ClientePrueba {
    public static void main(String[] args) {
        try {
            // get a datagram socket
            DatagramSocket socket = new DatagramSocket();
            // send request
            byte[] buf = new byte[256];
            String envio = "Trost";
            buf = envio.getBytes();
            InetAddress address = getByName("127.0.0.0");
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 9000);
            socket.send(packet);

            // get response
            buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            // display response
            String received = new String(packet.getData()).trim();
            System.out.println("Mensaje de server" + received);

            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
