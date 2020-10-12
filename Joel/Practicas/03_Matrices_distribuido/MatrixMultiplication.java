/**
 * Joel Hernández @ 2020
 * Github: https://github.com/JoelHernandez343
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class MatrixMultiplication {

    static final int NODES = 4;
    static int N;
    static int [][] A, B, A1, A2, B1, B2;

    // Long because there are overflow with N = 1000
    static long [][] C;
    
    static class Worker extends Thread {

        static Object lock = new Object();

        Socket conn;
        int node;

        Worker(Socket conn, int node) { this.conn = conn; this.node = node; }

        public void run(){
            try {
                System.out.println("[Server] Connected to node " + node);

                DataOutputStream output = new DataOutputStream(conn.getOutputStream());
                DataInputStream input = new DataInputStream(conn.getInputStream());

                // N is sent to all nodes
                output.writeInt(N);
                output.flush();

                // Copy the auxiliary matrices into a byte buffer
                ByteBuffer aux1 = ByteBuffer.allocate(N * N * 2);
                ByteBuffer aux2 = ByteBuffer.allocate(N * N * 2);

                int a = node == 1 || node == 2 ? 0 : N / 2;
                int b = node == 1 || node == 3 ? 0 : N / 2;

                for (int i = 0; i < N / 2; ++i){
                    for (int j = 0; j < N; ++j){
                        aux1.putInt(A[i + a][j]);
                        aux2.putInt(B[i + b][j]);
                    }
                }

                // The matrices are sent
                output.write(aux1.array());
                output.flush();

                output.write(aux2.array());
                output.flush();

                // The Cn part is received
                byte [] c = new byte[N * N * 2];
                read(input, c, 0, N * N * 2);
                ByteBuffer cc = ByteBuffer.wrap(c);

                // The Cn part is copied. Critical section
                synchronized(lock){
                    for (int i = 0; i < N / 2; ++i){
                        for (int j = 0; j < N / 2; ++j){
                            C[i + a][j + b] = cc.getLong();
                        }
                    }
                }

                output.close();
                input.close();
                conn.close();
            }
            catch (Exception e) {
                System.err.println(ConsoleColors.ERR + " " + e.getMessage());
            }
        }
    }

    public static void main(String [] args) throws Exception {

        // Check if the number of CLI arguments are correct
        if (args.length < 1 || args.length > 2){
            System.err.println(ConsoleColors.ERR + " Node number or N not provided. Usage: ");
            System.err.println("java " + MatrixMultiplication.class.getName() + " 0 <4|1000> | <1|2|3|4>");
            System.exit(1);
        }

        // Try to convert the node string into integer
        int node = 0;
        try {
            node = Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            System.err.println(ConsoleColors.ERR + " Wrong node format, node must be a number. Usage: ");
            System.err.println("java " + MatrixMultiplication.class.getName() + " 0 <4|1000> | <1|2|3|4>");
            System.exit(1);
        }

        // Server side
        if (node == 0){
            // Check if the given N is allowed
            try {
                N = Integer.valueOf(args[1]);

                if (N != 4 && N != 1000) throw new Exception();
            } catch (Exception e) {
                System.err.println(ConsoleColors.ERR + " Wrong matriz size or not provided. Sizes allowed: 4 | 1000");
                System.exit(1);
            }
            rootSide();
        } else {
            nodeSide(node);
        }
    }

    // Initializes the matrices
    public static void rootInitialization(){
        A = new int[N][N];
        B = new int[N][N];
        C = new long[N][N];

        // Matrices are filled
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                A[i][j] = 2 * i + j;
                B[i][j] = 2 * i - j;
                C[i][j] = 0;
            }
        }

        // Transpose matrix B
        for (int i = 0; i < N; ++i){
            for (int j = i + 1; j < N; ++j){
                int x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }
    }

    // Results
    public static void showResults(){
        long checksum = 0;
        
        for (int i = 0; i < N; ++i){
            for (int j = 0; j < N; ++j){
                checksum += C[i][j];
            }
        }

        System.out.println("The checksum is: " + checksum);

        if (N != 4) return;

        for (int i = 0; i < N; ++i){
            for (int j = 0; j < N; ++j){
                System.out.print(C[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public static void rootSide() throws Exception {
        // Initialize the matrices
        rootInitialization();

        ServerSocket server = new ServerSocket(30000);
        
        // Accept all nodes connections
        Worker [] workers = new Worker[4];

        for (int i = 0; i < NODES; ++i){
            workers[i] = new Worker(server.accept(), i);
            workers[i].start();
        }

        // Wait for all nodes
        for (int i = 0; i < NODES; ++i){
            workers[i].join();
        }

        showResults();
        
        server.close();
    }

    // Initializes the matrices
    public static void nodeInitialization() throws Exception{
        A = new int[N / 2][N];
        B = new int[N / 2][N];
        C = new long[N / 2][N / 2];

        for (int i = 0; i < N / 2; ++i){
            for (int j = 0; j < N / 2; ++j){
                C[i][j] = 0;
            }
        }
    }

    public static void nodeSide(int node) throws Exception {
        Socket conn;
        System.out.println("Trying connecting to localhost at 30000");
        while (true) {
            try {
                conn = new Socket("localhost", 30000);
                break;
            }
            catch (Exception e) {
                Thread.sleep(100);
            }
        }

        DataOutputStream output = new DataOutputStream(conn.getOutputStream());
        DataInputStream input = new DataInputStream(conn.getInputStream());

        // N is received
        N = input.readInt();
        nodeInitialization();

        // An and Bn are received
        byte [] array1 = new byte [N * N * 2];
        byte [] array2 = new byte [N * N * 2];

        read(input, array1, 0, N * N * 2);
        read(input, array2, 0, N * N * 2);

        ByteBuffer aux1 = ByteBuffer.wrap(array1);
        ByteBuffer aux2 = ByteBuffer.wrap(array2);

        for (int i = 0; i < N / 2; ++i){
            for (int j = 0; j < N; ++j){
                A[i][j] = aux1.getInt();
                B[i][j] = aux2.getInt();
            }
        }

        // Calculation and sending
        ByteBuffer c = ByteBuffer.allocate(N * N * 2);

        for (int i = 0; i < N / 2; ++i){
            for (int j = 0; j < N / 2; ++j){
                for (int k = 0; k < N; ++k){
                    C[i][j] += A[i][k] * B[j][k];
                }

                c.putLong(C[i][j]);
            }
        }
        
        output.write(c.array());
        output.flush();

        output.close();
        input.close();
        conn.close();
    }

    // Auxiliary method to read
    static void read(DataInputStream f, byte[] b, int position, int length) throws Exception {
        while (length > 0) {
            int n = f.read(b, position, length);
            position += n;
            length -= n;
        }
    }
}
