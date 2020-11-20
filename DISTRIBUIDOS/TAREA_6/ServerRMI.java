import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI {
    static int N, PORT;

    static class ClassRMI extends UnicastRemoteObject implements InterfaceRMI {

        public ClassRMI() throws RemoteException {
            super();
        }

        public int[][] multiplica_matrices(int[][] A, int[][] B) throws RemoteException {
            int[][] C = new int[N / 2][N / 2];
            for (int i = 0; i < N / 2; i++)
                for (int j = 0; j < N / 2; j++)
                    for (int k = 0; k < N; k++)
                        C[i][j] += A[i][k] * B[j][k];
            return C;
        }
    }

    public static void main(String[] args) throws Exception {

        String url = "rmi://localhost:" + PORT + "/prueba";
        System.out.println(url);
        ClassRMI obj = new ClassRMI();

        Naming.rebind(url, obj);
    }
}
