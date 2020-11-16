//Elias_chat.java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
class Chat
{
    static String ip = "230.0.0.0";
    static int puerto=50000;


    static void envia_mensaje(byte[] buffer,String ip,int puerto) throws IOException
    {
      DatagramSocket socket = new DatagramSocket();
      InetAddress grupo = InetAddress.getByName(ip);
      DatagramPacket paquete = new DatagramPacket(buffer,buffer.length,grupo,puerto);
      socket.send(paquete);
      socket.close();
    }
    static byte[] recibe_mensaje(MulticastSocket socket,int longitud_mensaje) throws IOException
    {
      byte[] buffer = new byte[longitud_mensaje];
      DatagramPacket paquete = new DatagramPacket(buffer,buffer.length);
      socket.receive(paquete);
      return paquete.getData();
    }

  static class Worker extends Thread
  {
    public void run()
    {
     // En un ciclo infinito se recibirán los mensajes enviados al grupo
     // 230.0.0.0 a través del puerto 50000 y se desplegarán en la pantalla.
     try{
         InetAddress grupo=InetAddress.getByName(ip);
         MulticastSocket socket  = new MulticastSocket(puerto);
         socket.joinGroup(grupo);

         while(true){
             byte[] mensaje = recibe_mensaje(socket,256);
             System.out.println(new String(mensaje,"UTF-8"));
         }
     }catch(Exception e){
         System.out.println(e.getMessage());
     }
    }
  }
  public static void main(String[] args) throws Exception
  {
    Worker w = new Worker();
    w.start();
    String nombre = args[0];
    BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
    // En un ciclo infinito se leerá los mensajes del teclado y se enviarán
    // al grupo 230.0.0.0 a través del puerto 50000.
    System.out.println("Bienvenido "+nombre);
    System.out.println("Escribe tu mensaje: ");
    while(true){
        String mensaje= nombre+ ":"+b.readLine();
        envia_mensaje(mensaje.getBytes(),ip,puerto);
    }
  }
}

