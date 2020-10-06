import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

class Cliente
{
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception
  {
    while (longitud > 0)
    {
      int n = f.read(b,posicion,longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception
  {
    double n;
    Socket conexion = new Socket("localhost",50000);
    
    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    // enva un entero de 32 bits
    salida.writeInt(123);

    // envia un numero punto flotante
    //salida.writeDouble(1234567890.1234567890);
    //envia 1000 punto flotante 
    long time = System.currentTimeMillis();
    for (n=0;n<10000;n++){
        salida.writeDouble(n);
        n+=1;
    }
    System.out.println("Tiempo total: " + (System.currentTimeMillis() - time));
    // envia una cadena
    salida.write("hola".getBytes());

    // recibe una cadena
    byte[] buffer = new byte[4];
    read(entrada,buffer,0,4);
    System.out.println(new String(buffer,"UTF-8"));

    // envia 5 numeros punto flotante
    ByteBuffer b = ByteBuffer.allocate(5*8);
    b.putDouble(1.1);
    b.putDouble(1.2);
    b.putDouble(1.3);
    b.putDouble(1.4);
    b.putDouble(1.5);
    byte[] a = b.array();
    
    
    salida.write(a);

    salida.close();
    entrada.close();
    conexion.close();    
  }
}
