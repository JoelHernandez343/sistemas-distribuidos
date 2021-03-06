import java.rmi.Naming;

public class ClientRMI {
    static int N;
    static int NODES = 4;

    static int[][] A;
    static int[][] B;
    static int[][] C;

    static int[][][] As = new int[2][][];
    static int[][][] Bs = new int[2][][];
    static int[][][] Cs = new int[4][][];

    static class Worker extends Thread {
        String ip;
        int node;

        Worker(String ip, int node) {
            this.ip = ip;
            this.node = node;
        }

        public void run() {
            try {
                String url = "rmi://" + ip + "/prueba";

                InterfaceRMI remote = (InterfaceRMI) Naming.lookup(url);

                int a = node == 0 || node == 1 ? 0 : 1;
                int b = node == 0 || node == 2 ? 0 : 1;

                Cs[node] = remote.multiplica_matrices(As[a], Bs[b]);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        validateArguments(args);

        initializeMatrix();

        for (int node = 0; node < NODES; ++node){
            String ip = args[node + 1];
            String url = "rmi://" + ip + "/prueba";

            InterfaceRMI remote = (InterfaceRMI) Naming.lookup(url);

            int a = node == 0 || node == 1 ? 0 : 1;
            int b = node == 0 || node == 2 ? 0 : 1;

            Cs[node] = remote.multiplica_matrices(As[a], Bs[b]);
        }

        // Worker[] workers = new Worker[4];

        // for (int i = 0; i < NODES; ++i) {
        //     workers[i] = new Worker(args[i + 1], i);
        //     workers[i].start();
        // }

        // for (int i = 0; i < NODES; ++i) {
        //     workers[i].join();
        // }

        showResults();
    }

    static void validateArguments(String[] args) {
        if (args.length != 5) {
            System.err.println(ConsoleColors.ERR + " Number of arguments incorrect. Usage: ");
            System.err.println(
                    "\tjava ClientRMI <size> <ip_node_0:port> <ip_node_1:port> <ip_node_2:port> <ip_node_3:port>");
            System.exit(1);
        }

        try {
            N = Integer.valueOf(args[0]);

            if (N != 4 && N != 500) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.err.println(ConsoleColors.ERR + " Wrong size format, node must be a number and be 4 or 500.");
            System.exit(1);
        }

        for (int i = 0; i < NODES; ++i) {
            if (!validateIpv4(args[i + 1])) {
                System.err.println(ConsoleColors.ERR + " Wrong ip:port format: \"" + args[i + 1] + "\".");
                System.exit(1);
            }
        }

        return;
    }

    static int[][] parte_matriz(int[][] A, int inicio) {
        int[][] M = new int[N / 2][N];
        for (int i = 0; i < N / 2; i++)
            for (int j = 0; j < N; j++)
                M[i][j] = A[i + inicio][j];
        return M;
    }

    static void acomoda_matriz(int[][] C, int[][] A, int renglon, int columna) {
        for (int i = 0; i < N / 2; i++)
            for (int j = 0; j < N / 2; j++)
                C[i + renglon][j + columna] = A[i][j];
    }

    static void initializeMatrix() {
        A = new int[N][N];
        B = new int[N][N];
        C = new int[N][N];

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                A[i][j] = 2 * i - j;
                B[i][j] = 2 * i + j;
            }
        }

        if (N == 4) {
            System.out.println("A:");
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < N; ++j) {
                    System.out.print(A[i][j] + " ");
                }
                System.out.println("");
            }

            System.out.println("B:");
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < N; ++j) {
                    System.out.print(B[i][j] + " ");
                }
                System.out.println("");
            }
        }

        // Transpose matrix B
        for (int i = 0; i < N; ++i) {
            for (int j = i + 1; j < N; ++j) {
                int x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }

        As[0] = parte_matriz(A, 0);
        As[1] = parte_matriz(A, N / 2);

        Bs[0] = parte_matriz(B, 0);
        Bs[1] = parte_matriz(B, N / 2);
    }

    static void showResults() {

        acomoda_matriz(C, Cs[0], 0, 0);
        acomoda_matriz(C, Cs[1], 0, N / 2);
        acomoda_matriz(C, Cs[2], N / 2, 0);
        acomoda_matriz(C, Cs[3], N / 2, N / 2);

        long checksum = 0;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                checksum += C[i][j];
            }
        }

        System.out.println("The checksum is: " + checksum);

        if (N != 4) {
            return;
        }

        System.out.println("C:");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                System.out.print(C[i][j] + " ");
            }
            System.out.println("");
        }
    }

    static boolean validateIpv4(final String ip) {
        String pattern = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?):\\d+$";

        return ip.matches(pattern);
    }
}
