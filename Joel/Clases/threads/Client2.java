import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Client2 {

    static void read(DataInputStream f, byte[] b, int position, int length) throws Exception {
        while (length > 0) {
            int n = f.read(b, position, length);
            position += n;
            length -= n;
        }
    }

    public static void main(String [] args) throws Exception {
        
        // Try to connect to the server until it's done
        Socket connection;
        System.out.println("Trying connecting to localhost at 50000");
        while (true) {
            try {
                connection = new Socket("localhost", 50000);
                break;
            }
            catch (Exception e) {
                Thread.sleep(100);
            }
        }

        DataOutputStream output = new DataOutputStream(connection.getOutputStream());
        DataInputStream input = new DataInputStream(connection.getInputStream());

        output.writeInt(123);
        output.writeDouble(1234567890.1234567890);
        output.write("Hola".getBytes());

        byte[] buffer = new byte[4];
        read(input, buffer, 0, 4);
        System.out.println(new String(buffer, "UTF-8"));

        ByteBuffer b = ByteBuffer.allocate(5 * 8);
        b.putDouble(1.1);
        b.putDouble(1.2);
        b.putDouble(1.3);
        b.putDouble(1.4);
        b.putDouble(1.5);
        byte[] a = b.array();
        output.write(a);

        output.close();
        input.close();
        connection.close();
    }

}
