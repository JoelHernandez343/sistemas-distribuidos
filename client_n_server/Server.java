import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server {
    static void read(DataInputStream f, byte[] b, int position, int length) throws Exception {
        while (length > 0) {
            int n = f.read(b, position, length);
            position += n;
            length -= n;
        }
    }
    
    public static void main(String [] args) throws Exception {
        ServerSocket server = new ServerSocket(50000);
        Socket connection = server.accept();

        DataOutputStream output = new DataOutputStream(connection.getOutputStream());
        DataInputStream input = new DataInputStream(connection.getInputStream());

        int n = input.readInt();
        System.out.println(n);

        double x = input.readDouble();
        System.out.println(x);

        byte[] buffer = new byte[4];
        read(input, buffer, 0, 4);
        System.out.println(new String(buffer, "UTF-8"));

        output.write("HOLA".getBytes());

        /* Read 10k doubles */
        /* long time = System.currentTimeMillis();

        for (int i = 0; i < 10000; ++i)
            System.out.println(input.readDouble());

        System.out.println("Tiempo total: " + (System.currentTimeMillis() - time)); */

        /* Read 10k doubles from buffer */
        long time = System.currentTimeMillis();

        byte[] a = new byte[10000 * 8];
        read(input, a, 0, 10000 * 8);

        ByteBuffer b = ByteBuffer.wrap(a);
        for (int i = 0; i < 10000; ++i)
            System.out.println(b.getDouble());

        System.out.println("Tiempo total: " + (System.currentTimeMillis() - time));

        // byte[] a = new byte[5 * 8];
        // read(input, a, 0, 5 * 8);
        // ByteBuffer b = ByteBuffer.wrap(a);
        // for (int i = 0; i < 5; ++i)
        //     System.out.println(b.getDouble());

        output.close();
        input.close();
        connection.close();

        server.close();
    }
}
