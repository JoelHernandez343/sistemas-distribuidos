import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Exercise {

    static void read(DataInputStream f, byte[] b, int position, int length) throws Exception {
        while (length > 0) {
            int n = f.read(b, position, length);
            position += n;
            length -= n;
        }
    }

    public static void main(String[] args) throws Exception {
        Socket connection = new Socket("sisdis.sytes.net", 10000);
        DataOutputStream output = new DataOutputStream(connection.getOutputStream());
        DataInputStream input = new DataInputStream(connection.getInputStream());

        output.writeInt(3);
        output.flush();

        output.writeInt(10);
        output.flush();

        output.writeInt(50);
        output.flush();

        System.out.println(input.readInt());

        connection.close();
    }

}