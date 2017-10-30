import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;

public class ClientePrueba {
    public static void main(String[] args) {
        String linea;
        Socket socket;
        BufferedReader entrada;
        try {
            socket = new Socket("127.0.0.1",9000);
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //salida.writeUTF("192.168.1.1\n");
            salida.writeUTF("Trost\n");
            salida.close();
            entrada.close();
            socket.close();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
}
