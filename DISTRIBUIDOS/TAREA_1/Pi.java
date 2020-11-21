//EliaSan
import java.io.*;
import java.net.*;

public class Pi{
    static Object lock = new Object();
    static double pi = 0;
    static class Worker extends Thread{
        Socket conexion;
        public Worker(Socket conexion) {this.conexion = conexion;}
    public void run()
    {
      // Algoritmo 1
      try{
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        double x = entrada.readDouble();
        synchronized(lock) {
            pi += x;
        }
        entrada.close();
        salida.close();
        }catch (Exception e){
            System.err.println(e.getMessage());}

    }
  }
  public static void main(String[] args) throws Exception
  {
    if (args.length != 1)
    {
      System.err.println("Uso:");
      System.err.println("java PI <nodo>");
      System.exit(0);
    }
    
    int nodo = Integer.valueOf(args[0]);
    if (nodo == 0)
    {
      // Algoritmo 2
      ServerSocket servidor = new ServerSocket(30000);
      Worker[] w = new Worker[3];
            int i = 0;
            while(!(i == 3)) {
                Socket conexion = servidor.accept();
                w[i] = new Worker(conexion);
                w[i].start();
                i += 1;
    }
    double suma =0;
    i =0;
    
    while (i != 10000000){
        suma = 4.0 / ((8 * i) + 1) + suma;
        i++;}
    synchronized(lock) {
        pi += suma;}
    i=0;
    while (i < 3){
        w[i].join();
        i++;}
    servidor.close();
    System.out.println("Valor de pi: " + pi);
    
    }else{
      // Algoritmo 3
      Socket conexion = null;
      for(;;){
        try {
            conexion = new Socket ("localhost",30000);
            break;
        }
        catch (Exception e){
                Thread.sleep(100);}}
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        
        double suma= 0;
        int i=0;
        while(i != 10000000){
            suma = 4.0 / ((8 * i) + (2 * (nodo - 1) + 3)) + suma;
            i += 1;
        }
        suma = (nodo % 2) == 0 ? suma : (-1) * suma;
        salida.writeDouble(suma);
        salida.flush();

        salida.close();
        conexion.close();
    }
  }
}
