import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Client{

    static void read(DataInputStream f, byte[] b, int position, int length) throws Exception {
        while (length > 0) {
            int n = f.read(b, position, length);
            position += n;
            length -= n;
        }
    }

    public static void main(String [] args) throws Exception {
        Socket connection = new Socket("localhost", 50000);

        DataOutputStream output = new DataOutputStream(connection.getOutputStream());
        DataInputStream input = new DataInputStream(connection.getInputStream());

        output.writeInt(123);
        output.writeDouble(1234567890.1234567890);
        output.write("hola".getBytes());

        byte[] buffer = new byte[4];
        read(input, buffer, 0, 4);
        System.out.println(new String(buffer, "UTF-8"));

        // Send 5 double numbers
        // ByteBuffer b = ByteBuffer.allocate(5 * 8);
        // b.putDouble(1.1);
        // b.putDouble(1.2);
        // b.putDouble(1.3);
        // b.putDouble(1.4);
        // b.putDouble(1.5);
        // byte[] a = b.array();
        // output.write(a);



        /* Write 10k doubles */
        /* long time = System.currentTimeMillis();

        for (int i = 0; i < 10000; ++i)
            output.writeDouble(1234567890.1234567890);

        System.out.println("Tiempo total: " + (System.currentTimeMillis() - time)); */




        /* Write 10k doubles with buffer */
        long time = System.currentTimeMillis();

        ByteBuffer b = ByteBuffer.allocate(10000 * 8);
        for (int i = 0; i < 10000; ++i)
            b.putDouble(1234567890.1234567890);
        byte[] a = b.array();
        output.write(a);
        output.flush();
        
        System.out.println("Tiempo total: " + (System.currentTimeMillis() - time));

        output.close();
        input.close();
        connection.close();
    }
}