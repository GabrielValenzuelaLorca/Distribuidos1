package DistritoServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.net.*;


public class ClienteMulti {
    static String ip;
    static String nombre;
    static DistritoServer distrito;
    static List<Titan> titanes = new ArrayList<Titan>();
    static ArrayList<ArrayList<String>> titanescapturados = new ArrayList<ArrayList<String>>();
    static ArrayList<ArrayList<String>> titanesasesinados = new ArrayList<ArrayList<String>>();

    public void Cliente(String ip, DistritoServer distrito,String nombre) {
        this.ip = ip;
        this.distrito = distrito;
        this.nombre=nombre;
    }

    public static class actualizartitanes implements Runnable {

        public void run() {
            try {
                while (true) {
                    String ipmulti = distrito.ip_multi;
                    int portmulti=distrito.puerto_multi;
                    MulticastSocket socket = new MulticastSocket(portmulti);
                    socket.joinGroup(InetAddress.getByName(ipmulti));
                    byte[] buffer = new byte[1000];
                    DatagramPacket packet= new DatagramPacket(buffer,buffer.length);
                    socket.receive(packet);
                    socket.close();
                    String mensaje= new String(buffer);
                    //Obtener datos de titan colosal
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void cambiar_distrito(DistritoServer distrito) {
        this.distrito = distrito;
    }

    public static void agregar_titan_capturado(Titan titan) throws IOException {
        ArrayList<String> titancapturado = new ArrayList<String>();
        String nombretitan=titan.nombre;
        String tipo=titan.tipo;
        String id=Integer.toString(titan.id);
        titancapturado.add(nombretitan);
        titancapturado.add(tipo);
        titancapturado.add(id);
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
        titanesasesinados.add(titanasesinado);
    }

    public static void eliminar_titan(int id) throws IOException {
        for (Iterator<Titan> iter = titanes.listIterator(); iter.hasNext(); ) {
            Titan a = iter.next();
            if (a.id==id) {
                iter.remove();
            }
        }
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

    public static void main(String[] args) {
        try {

            int port = distrito.puerto_recep;
            String address = distrito.ip_recep;
            Thread hebra_titanes = new Thread(new actualizartitanes());
            hebra_titanes.start();
            boolean flag = true;
            /*while (flag){
            if (!hebra_titanes.isAlive()){
                flag=false;
            }
            }*/
            String opcion;
            int i;
            BufferedReader datos = new BufferedReader(new InputStreamReader(System.in));
            System.out.println(nombre +" (1) LISTAR TITANES");
            System.out.println(nombre +" (2) CAMBIAR DISTRITO");
            System.out.println(nombre +" (3) CAPTURAR TITAN");
            System.out.println(nombre +" (4) ASESINAR TITAN");
            System.out.println(nombre +" (5) LISTAR TITANES CAPTURADOS");
            System.out.println(nombre +" (6) LISTAR TITANES ASESINADOS");
            System.out.println(nombre +" (7) SALIR\n>");
            opcion = datos.readLine();
            if ("1".equals(opcion)) {
                for(i=0;i<titanes.size();i++)
                {
                    System.out.println(titanes.get(i).id +" "+titanes.get(i).nombre+" "+titanes.get(i).tipo );
                }

            } else if ("2".equals(opcion)) {
                //DISTRITO
            } else if ("3".equals(opcion)) {
                //CAPTURAR
                BufferedReader data = new BufferedReader(new InputStreamReader(System.in));
                System.out.println(nombre +"Ingresar ID del Titan");
                int id = Integer.valueOf(data.readLine());
                agregar_titan_capturado(buscar_titan(id));
                eliminar_titan(id);
            } else if ("4".equals(opcion)) {
                //ASESINAR
                System.out.println(nombre +"Ingresar ID del Titan");
                int id = Integer.valueOf(datos.readLine());
                agregar_titan_asesinado(buscar_titan(id));
                eliminar_titan(id);

            } else if ("5".equals(opcion)) {
                for(i=0;i<titanescapturados.size(); i++)
                {
                    System.out.println(titanescapturados.get(i));
                }
            } else if ("6".equals(opcion)) {
                for(i=0;i<titanesasesinados.size();i++)
                {
                    System.out.println(titanesasesinados.get(i));
                }
            } else if ("7".equals(opcion)) {
                //salir
            } else {
                System.out.println("Ingrese una opcion valida");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}