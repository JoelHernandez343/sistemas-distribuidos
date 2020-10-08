public class MatrixMultiplication {
    static int N;
    static int[][] A, B, C;

    public static void main(String [] args) {
        if (args.length != 1) {
            System.err.println(ConsoleColors.ERR + " Number N not provided. Usage: ");
            System.err.println("java " + MatrixMultiplication.class.getName() + " <N>");
            System.exit(1);
        }

        try {
            N = Integer.valueOf(args[0]);
            A = new int[N][N];
            B = new int[N][N];
            C = new int[N][N];
        } catch (NumberFormatException e) {
            System.err.println(ConsoleColors.ERR + " Wrong format, node must be a number. Usage: ");
            System.err.println("java " + MatrixMultiplication.class.getName() + " <node>");
            System.exit(1);
        }

        long time = System.currentTimeMillis();

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                A[i][j] = 2 * i - j;
                B[i][j] = i + 2 * j;
                C[i][j] = 0;
            }
        }

        for (int i = 0; i < N; ++i){
            for (int j = 0; j < N; ++j){
                for (int k = 0; k < N; ++k){
                    C[i][j] += A[i][k] * B[k][i];
                }
            }
        }

        System.out.println("Elapsed time for Matrix 01: " + (System.currentTimeMillis() - time) + " ms.");
    }
}