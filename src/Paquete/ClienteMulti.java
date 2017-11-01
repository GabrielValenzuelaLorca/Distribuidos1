package Paquete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.net.*;

import static java.net.InetAddress.getByName;


public class ClienteMulti {
    static String nombre="[Cliente] ";
    static Distrito distrito;
    static InetAddress ip_serv;
    static int puerto_serv;
    static List<Titan> titanes = new ArrayList<Titan>();
    static ArrayList<ArrayList<String>> titanescapturados = new ArrayList<ArrayList<String>>();
    static ArrayList<ArrayList<String>> titanesasesinados = new ArrayList<ArrayList<String>>();

    public static class actualizartitanes implements Runnable {

        public void run() {
            try {
                MulticastSocket socket;
                byte[] buffer;
                DatagramPacket packet;
                while (true) {

                    socket = new MulticastSocket(distrito.getPuerto_multi());
                    socket.joinGroup(distrito.getIp_multi());
                    buffer = new byte[10000];
                    packet= new DatagramPacket(buffer,buffer.length);
                    socket.receive(packet);
                    socket.close();
                    String mensaje= new String(packet.getData()).trim();

                    if ("%".equals(mensaje.substring(0,1))){
                        List<Titan> nuevos = new ArrayList<Titan>();
                        String[] lista=mensaje.substring(1,mensaje.length()).split("-");
                        if (mensaje.length()>1){
                            for (int i=0;i<lista.length;i++){
                                String[] elem=lista[i].split(" ");
                                Titan nuevo_titan= new Titan(Integer.parseInt(elem[0].trim()),elem[1].trim(),elem[2].trim());
                                nuevos.add(nuevo_titan);
                            }
                            titanes=nuevos;
                        }
                        else{
                            titanes=new ArrayList<Titan>();
                        }


                    }else{
                        System.out.println(mensaje);
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class menu implements Runnable{

        public void run() {
            DatagramPacket packet;
            DatagramSocket socket;
            byte[] buf;
            String ip_recep, ip_multi, opcion, nombre_distrito;
            int puerto_recep, puerto_multi;
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

            try {
                while (true) {
                    BufferedReader datos = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println(nombre + "(1) LISTAR TITANES");
                    System.out.println(nombre + "(2) CAMBIAR DISTRITO");
                    System.out.println(nombre + "(3) CAPTURAR TITAN");
                    System.out.println(nombre + "(4) ASESINAR TITAN");
                    System.out.println(nombre + "(5) LISTAR TITANES CAPTURADOS");
                    System.out.println(nombre + "(6) LISTAR TITANES ASESINADOS");

                    opcion = datos.readLine();


                    if ("1".equals(opcion)) {
                        for (int i = 0; i < titanes.size(); i++) {
                            System.out.println(titanes.get(i).id + " " + titanes.get(i).nombre + " " + titanes.get(i).tipo);
                        }

                    } else if ("2".equals(opcion)) {
                        boolean aprobado = true;
                        while (aprobado) {
                            System.out.println("\n" + nombre + "Introducir Nombre de Distrito a Investigar, Ej: Trost, Shiganshina\n>");
                            opcion = datos.readLine();
                            nombre_distrito = opcion.trim();

                            socket = new DatagramSocket();
                            buf = new byte[256];
                            buf = nombre_distrito.getBytes();
                            packet = new DatagramPacket(buf, buf.length, ip_serv, puerto_serv);
                            socket.send(packet);

                            // get response
                            buf = new byte[256];
                            packet = new DatagramPacket(buf, buf.length);
                            socket.receive(packet);
                            socket.close();

                            // display response
                            String respuesta = new String(packet.getData()).trim();

                            if ("Rechazada".equals(respuesta)) {
                                System.out.println("Su peticion fue rechazada");
                            } else {
                                aprobado = false;
                                String[] parts = respuesta.replace("/", "").split(" ");
                                ip_multi = parts[1].trim();
                                puerto_multi = Integer.parseInt(parts[2].trim());
                                ip_recep = parts[3].trim();
                                puerto_recep = Integer.parseInt(parts[4].trim());
                                distrito = new Distrito(nombre_distrito, ip_multi, puerto_multi, ip_recep, puerto_recep);
                            }
                        }

                    } else if ("3".equals(opcion)) {
                        //CAPTURAR
                        System.out.println(nombre + "Ingresar ID del Titan");
                        int id = Integer.valueOf(datos.readLine());
                        Titan titan = buscar_titan(id);
                        if (titan!=null && ("Normal".equals(titan.tipo)||"Cambiante".equals(titan.tipo))){
                            agregar_titan_capturado(titan);
                            enviar_titan(titan.id,0,titan.nombre,titan.tipo);
                        }
                        else{
                            System.out.println(nombre+"Ingrese un titan valido");
                        }


                    } else if ("4".equals(opcion)) {
                        //ASESINAR
                        System.out.println(nombre + "Ingresar ID del Titan");
                        int id = Integer.valueOf(datos.readLine());
                        Titan titan = buscar_titan(id);
                        if (titan!=null && ("Normal".equals(titan.tipo)||"Excentrico".equals(titan.tipo))){
                            agregar_titan_asesinado(titan);
                            enviar_titan(titan.id,1,titan.nombre,titan.tipo);
                        }
                        else{
                            System.out.println(nombre+"Ingrese un titan valido");
                        }

                    } else if ("5".equals(opcion)) {
                        for (int i = 0; i < titanescapturados.size(); i++) {
                            System.out.println(titanescapturados.get(i));
                        }
                    } else if ("6".equals(opcion)) {
                        for (int i = 0; i < titanesasesinados.size(); i++) {
                            System.out.println(titanesasesinados.get(i));
                        }
                    } else {
                        System.out.println("Ingrese una opcion valida");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void agregar_titan_capturado(Titan titan) throws IOException {
        ArrayList<String> titancapturado = new ArrayList<String>();
        String nombretitan=titan.nombre;
        String tipo=titan.tipo;
        String id=Integer.toString(titan.id);
        titancapturado.add(nombretitan);
        titancapturado.add(tipo);
        titancapturado.add(id);
        titancapturado.add(distrito.nombre);
        titanescapturados.add(titancapturado);
    }

    public static void agregar_titan_asesinado(Titan titan) throws IOException {
        ArrayList<String> titanasesinado = new ArrayList<String>();
        String nombretitan = titan.nombre;
        String tipo = titan.tipo;
        String id = Integer.toString(titan.id);
        titanasesinado.add(nombretitan);
        titanasesinado.add(tipo);
        titanasesinado.add(id);
        titanasesinado.add(distrito.nombre);
        titanesasesinados.add(titanasesinado);
    }

    public static Titan buscar_titan(int id) throws IOException {
        Titan titan=null;
        for (Iterator<Titan> iter = titanes.listIterator(); iter.hasNext(); ) {
            Titan a = iter.next();
            if (a.id==id) {
                titan=a;
            }
        }
        return titan;
    }

    public static void enviar_titan(int id, int flag, String nombre, String tipo){
        DatagramSocket socket;
        DatagramPacket packet;
        byte[] buf;
        String envio;
        try {
            socket = new DatagramSocket();
            buf = new byte[1000];
            envio = String.valueOf(flag)+" "+String.valueOf(id)+" "+nombre+" "+tipo;
            buf = envio.getBytes();
            packet = new DatagramPacket(buf, buf.length, distrito.getIp_recep(), distrito.getPuerto_recep());
            socket.send(packet);
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        DatagramPacket packet;
        DatagramSocket socket;
        byte[] buf;
        boolean aprobado=true;
        String ip_recep, ip_multi,entrada, nombre_distrito;
        int puerto_recep,puerto_multi;
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (aprobado) {
                System.out.println(nombre + "Ingresar IP Servidor Central\n>");
                entrada = bf.readLine();
                ip_serv = getByName(entrada.trim());
                System.out.println(nombre + "Ingresar Puerto Servidor Central\n>");
                entrada = bf.readLine();
                puerto_serv = Integer.parseInt(entrada.trim());
                System.out.println("\n" + nombre + "Introducir Nombre de Distrito a Investigar, Ej: Trost, Shiganshina\n>");
                entrada = bf.readLine();
                nombre_distrito = entrada.trim();

                socket = new DatagramSocket();
                buf = new byte[256];
                buf = nombre_distrito.getBytes();
                packet = new DatagramPacket(buf, buf.length, ip_serv,puerto_serv);
                socket.send(packet);

                // get response
                buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                socket.close();

                // display response
                String respuesta = new String(packet.getData()).trim();

                if ("Rechazada".equals(respuesta)){
                    System.out.println("Su peticion fue rechazada");
                }
                else{
                    aprobado=false;
                    String[] parts=respuesta.replace("/","").split(" ");
                    ip_multi=parts[1].trim();
                    puerto_multi=Integer.parseInt(parts[2].trim());
                    ip_recep=parts[3].trim();
                    puerto_recep=Integer.parseInt(parts[4].trim());
                    distrito = new Distrito(nombre_distrito,ip_multi,puerto_multi,ip_recep,puerto_recep);
                }



            }

            Thread hebra_titanes = new Thread(new actualizartitanes());
            Thread hebra_menu = new Thread(new menu());
            hebra_titanes.start();
            hebra_menu.start();


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}