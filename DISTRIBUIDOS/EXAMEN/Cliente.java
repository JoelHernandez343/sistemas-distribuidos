import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

class Cliente{
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception{
    while (longitud > 0){
      int n = f.read(b,posicion,longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception{
    Socket conexion = new Socket("sisdis.sytes.net",10000);

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    // enva un entero de 32 bits
    salida.writeInt(1);
    salida.writeInt(10);
    // envia un numero punto flotante
    salida.writeDouble(100.0);
    System.out.println(entrada.readDouble());

    salida.close();
    entrada.close();
    conexion.close();
  }
}