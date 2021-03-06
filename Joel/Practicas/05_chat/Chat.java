import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

class Chat {

    static final String ip = "230.0.0.0";
    static final int port = 50000;

    static byte[] recibe_mensaje(MulticastSocket socket,int longitud) throws IOException{
        byte[] buffer = new byte[longitud];
        DatagramPacket paquete = new DatagramPacket(buffer,buffer.length);
        socket.receive(paquete);
        return buffer;
    }

    static void envia_mensaje(byte[] buffer,String ip,int puerto) throws IOException
    {
        DatagramSocket socket = new DatagramSocket();
        InetAddress grupo = InetAddress.getByName(ip);
        DatagramPacket paquete = new DatagramPacket(buffer,buffer.length,grupo,puerto);
        socket.send(paquete);
        socket.close();
    }

    static class Worker extends Thread {
        public void run(){
            // En un ciclo infinito se recibirán los mensajes enviados al grupo 
            // 230.0.0.0 a través del puerto 50000 y se desplegarán en la pantalla.
            try {
                InetAddress grupo = InetAddress.getByName(ip);
                MulticastSocket socket = new MulticastSocket(port);
                socket.joinGroup(grupo);

                while (true){
                    byte[] a = recibe_mensaje(socket, 256);
                    System.out.println(new String(a,"UTF-8"));
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    public static void main(String[] args) throws Exception {
        Worker w = new Worker();
        w.start();

        if (args.length == 0){
            System.out.println("Tienes que ingresar un parámetro.");
            return;
        }

        String nombre = args[0];
        
        System.out.println(nombre);
        BufferedReader b = new BufferedReader(new InputStreamReader(System.in));

        // En un ciclo infinito se leerá los mensajes del teclado y se enviarán
        // al grupo 230.0.0.0 a través del puerto 50000.
    
        while (true){
            System.out.println("Escribe el mensaje: ");
            String message = nombre  +  ":" + b.readLine();

            envia_mensaje(message.getBytes(), ip, port);
        }
    }
}