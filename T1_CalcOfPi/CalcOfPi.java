import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CalcOfPi {

    static Object flag = new Object();
    static double pi = 0;
    
    static class Worker extends Thread {
        Socket conn;
        int node;
    
        Worker(Socket conn, int node) { this.conn = conn; this.node = node; }

        public void run() {
            try {
                System.out.println("[Server] Connected to node " + node);

                DataInputStream input = new DataInputStream(conn.getInputStream());

                double x = input.readDouble();

                synchronized(flag) { pi += x; }

                input.close();
                conn.close();
            } catch (Exception e) {
                System.err.println(ConsoleColors.ERR + " " + e.getMessage());
            }
        }
    }

    public static void main(String [] args) throws Exception {
        if (args.length != 1) {
            System.err.println(ConsoleColors.ERR + " Node number not provided. Usage: ");
            System.err.println("java " + CalcOfPi.class.getName() + " <node>");
            System.exit(1);
        }

        int node = 0;
        try {
            node = Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            System.err.println(ConsoleColors.ERR + " Wrong format, node must be a number. Usage: ");
            System.err.println("java " + CalcOfPi.class.getName() + " <node>");
            System.exit(1);
        }

        if (node == 0) {
            rootSide();
        } 
        else {
            nodeSide(node);
        }
    }

    public static void rootSide() throws Exception {
        ServerSocket server = new ServerSocket(50000);
        
        Worker [] workers = new Worker[3];

        for (int i = 0; i < 3; ++i) {
            workers[i] = new Worker(server.accept(), i);
            workers[i].start();
        }

        double sum = 0.0;

        for (int i = 0; i < 10000000; ++i){
            sum += 4.0 / (8 * i + 1);
        }

        synchronized(flag) { pi += sum; }

        for (int i = 0; i < 3; ++i) {
            workers[i].join();
        }

        System.out.println(ConsoleColors.SUCCESS + " The PI value aproximated is: " + pi);
        
        server.close();
    }

    public static void nodeSide(int node) throws Exception {
        Socket conn;
        System.out.println("Trying connecting to localhost at 50000");
        while (true) {
            try {
                conn = new Socket("localhost", 50000);
                break;
            }
            catch (Exception e) {
                Thread.sleep(100);
            }
        }

        DataOutputStream output = new DataOutputStream(conn.getOutputStream());

        double sum = 0;

        for (int i = 0; i < 10000000; ++i){
            sum += 4.0 / (8 * i + 2 * (node - 1) + 3);
        }

        sum *= node % 2 == 0 ? 1 : -1;

        output.writeDouble(sum);

        output.close();
        conn.close();
    }

}