//Token-ring 0-3
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;


class Token
{
  static DataInputStream entrada;
  static DataOutputStream salida;
  static boolean primera_vez = true;
  static String ip;
  static long token = 0;
  static int nodo;

  static class Worker extends Thread
  {
    public void run()
    {
       //Algoritmo 1
       try{
            ServerSocket server=new ServerSocket(50000);
            Socket conexion = server.accept();
            entrada = new DataInputStream(conexion.getInputStream());
            
       }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
  }

  public static void main(String[] args) throws Exception
  {
    if (args.length != 2)
    {
      System.err.println("Se debe pasar como parametros el numero de nodo y la IP del siguiente nodo");
      System.exit(1);
    }

    nodo = Integer.valueOf(args[0]);  // el primer parametro es el numero de nodo
    ip = args[1];  // el segundo parametro es la IP del siguiente nodo en el anillo

    //Algoritmo 2
    
    Worker worker = new Worker();
    worker.start();
    Socket conexion;
    
    while (true){
        try {
            conexion= new Socket (ip,50000);
            break;
        }catch(Exception e) {
            Thread.sleep(500);
        }
    }
    
    System.out.println("Nodo: "+nodo+"Conectado a la ip : "+ip);
    
    salida = new DataOutputStream(conexion.getOutputStream());
    worker.join();
    
    while(true){
        if(nodo == 0 && primera_vez){
            primera_vez=false;
            token = entrada.readLong();
        }else {
            if(primera_vez){
                primera_vez=false;
        }
        token = entrada.readLong();
        }
        token++;
        System.out.println("Nodo : "+nodo+" Token: "+token);
        salida.writeLong(token);
        salida.flush();
    }
  }
}
