//ELIAS SAN 
//Matrices_Distribuidas_MAIN
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MatrizMultiplicacion{
    static final int NODES=4;
    static int n;
    static int [][] A,B,A1,A2,B1,B2;
    
    static long [][] C; //long por el overflow cuando n=1000
    static class Worker extends Thread{
        static Object lock =new Object();
        Socket conexion;
        int node;
        
        Worker(Socket conexion,int node){
            this.conexion=conexion;
            this.node=node;
        }
    public void run(){
        try{
            System.out.println("Conectandose a nodos... "+node+"[ok]");
            DataOutputStream output = new DataOutputStream(conexion.getOutputStream());
            DataInputStream input = new DataInputStream(conexion.getInputStream());
            
            output.writeInt(n);
            output.flush();

            //lo tratamos  como un arreglo debytes
            ByteBuffer aux1 = ByteBuffer.allocate(n * n * 2);
            ByteBuffer aux2 = ByteBuffer.allocate(n * n * 2);
            
            int a = node == 1 || node == 2 ? 0 : n / 2;
            int b = node == 1 || node == 3 ? 0 : n / 2;

            for (int i = 0; i < n / 2; ++i){
                for (int j = 0; j < n; ++j){
                    aux1.putInt(A[i + a][j]);
                    aux2.putInt(B[i + b][j]);
                    }
            }

            //envio de matrices
            output.write(aux1.array());
            output.flush();

            output.write(aux2.array());
            output.flush();

            // La parte de Cn es recivida
            byte [] c = new byte[n * n * 2];
            read(input, c, 0, n * n * 2);
            ByteBuffer cc = ByteBuffer.wrap(c);

            // la pasamos a la matriz C
            synchronized(lock){
            for (int i = 0; i < n / 2; ++i){
                for (int j = 0; j < n / 2; ++j){
                    C[i + a][j + b] = cc.getLong();
                    }
                }
            }

                output.close();
                input.close();
                conexion.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
      
    }
}

public static void main(String [] args) throws Exception {
    int node =0;
    node =Integer.valueOf(args[0]);
    //Server
    if (node == 0){
        n = Integer.valueOf(args[1]);
        ServerSide();
    } else {
            NodeSide(node);
        }
    }
    //inicializamos las matrices A,B,C
    public static void matrizInicializar(){
        A = new int[n][n];
        B = new int[n][n];
        C = new long[n][n];

        // Llenado de matrices
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                A[i][j] = 2 * i + j;
                B[i][j] = 2 * i - j;
                C[i][j] = 0;
            }
        }

        // Transponer la matriz B
        for (int i = 0; i < n; ++i){
            for (int j = i + 1; j < n; ++j){
                int x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }
    }
    //Resultados
    public static void verResultados(){
        long checksum = 0;
        
        for (int i = 0; i < n; ++i){
            for (int j = 0; j < n; ++j){
                checksum += C[i][j];
            }
        }

        System.out.println("The checksum is: " + checksum);

        if (n != 4) return;

        for (int i = 0; i < n; ++i){
            for (int j = 0; j < n; ++j){
                System.out.print(C[i][j] + " ");
            }
            System.out.println("");
        }
    }
    public static void ServerSide() throws Exception {
        // Inicializar las matrices 
        matrizInicializar();

        ServerSocket server = new ServerSocket(30000);
        
        // Aceptar los nodos
        Worker [] workers = new Worker[4];

        for (int i = 0; i < NODES; ++i){
            workers[i] = new Worker(server.accept(), i);
            workers[i].start();
        }

        // Espera de los nodos
        for (int i = 0; i < NODES; ++i){
            workers[i].join();
        }

        verResultados();
        
        server.close();
    }
 // Inicializar la matriz
    public static void nodeInicializar() throws Exception{
        A = new int[n / 2][n];
        B = new int[n / 2][n];
        C = new long[n / 2][n / 2];

        for (int i = 0; i < n / 2; ++i){
            for (int j = 0; j < n / 2; ++j){
                C[i][j] = 0;
            }
        }
    }

    public static void NodeSide(int node) throws Exception {
        Socket conexion;
        System.out.println("Trying connecting to localhost at 30000");
        while (true) {
            try {
                conexion = new Socket("localhost", 30000);
                break;
            }
            catch (Exception e) {
                Thread.sleep(100);
            }
        }

        DataOutputStream output = new DataOutputStream(conexion.getOutputStream());
        DataInputStream input = new DataInputStream(conexion.getInputStream());
        
        // n es recivida 
        n = input.readInt();
        nodeInicializar();

        // An y Bn recividos
        byte [] array1 = new byte [n * n * 2];
        byte [] array2 = new byte [n * n * 2];

        read(input, array1, 0, n * n * 2);
        read(input, array2, 0, n * n * 2);

        ByteBuffer aux1 = ByteBuffer.wrap(array1);
        ByteBuffer aux2 = ByteBuffer.wrap(array2);

        for (int i = 0; i < n / 2; ++i){
            for (int j = 0; j < n; ++j){
                A[i][j] = aux1.getInt();
                B[i][j] = aux2.getInt();
            }
        }

        // Calcular y enviar a C
        ByteBuffer c = ByteBuffer.allocate(n * n * 2);

        for (int i = 0; i < n / 2; ++i){
            for (int j = 0; j < n / 2; ++j){
                for (int k = 0; k < n; ++k){
                    C[i][j] += A[i][k] * B[j][k];
                }

                c.putLong(C[i][j]);
            }
        }
        
        output.write(c.array());
        output.flush();

        output.close();
        input.close();
        conexion.close();
    }

    // Metodo para leer las posicion
    static void read(DataInputStream f, byte[] b, int position, int length) throws Exception {
        while (length > 0) {
            int n = f.read(b, position, length);
            position += n;
            length -= n;
        }
    }
}
