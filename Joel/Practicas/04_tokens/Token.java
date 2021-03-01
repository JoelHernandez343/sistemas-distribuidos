/**
 * Joel Hernández @ 2020
 * Github: https://github.com/JoelHernandez343
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Token {

    static final int PORT = 50000;

    static DataInputStream input;
    static DataOutputStream output;
    static boolean firstTime = true;
    static String ip;
    static long token = 0;
    static int node;

    static class Worker extends Thread {
        public void run(){
            try {

                System.out.println("[Node " + node + "] Awaiting connection.");

                ServerSocket server = new ServerSocket(PORT);
                Socket conn = server.accept();
                input = new DataInputStream(conn.getInputStream());

                System.out.println("[Node " + node + "] Connection received.");

            } catch (Exception e){
                System.err.println(ConsoleColors.ERR + " " + e.getMessage());
                System.exit(1);
            }
        }
    }

    public static boolean validateIpv4(final String ip){
        String pattern = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip.matches(pattern);
    }

    public static void main(String[] args) throws Exception {

        // Arguments validation
        if (args.length != 2){
            System.err.println(ConsoleColors.ERR + " Usage: <node> <next_ip>");
            System.exit(1);
        }

        try {
            node = Integer.valueOf(args[0]);

            if (node < 0 || node > 3) throw new Exception();
        } catch (Exception e){
            System.err.println(ConsoleColors.ERR + " Node format error. Node must be between [0 - 3].");
            System.exit(1);
        }

        if (!validateIpv4(args[1])){
            System.err.println(ConsoleColors.ERR + " IP format error.");
            System.exit(1);
        }

        ip = args[1];

        process();
    }

    public static void process() throws Exception{
        Worker worker = new Worker();
        worker.start();

        Socket conn;
        System.out.println("[Node " + node + "] Trying to connect to " + ip + " at " + PORT);

        while (true){
            try {
                conn = new Socket(ip, PORT);
                break;
            } catch (Exception e){
                Thread.sleep(500);
            }
        }

        System.out.println("[Node " + node + "] Connected to " + ip + " at " + PORT);

        output = new DataOutputStream(conn.getOutputStream());
        worker.join();

        System.out.println("[Node " + node + "] Ready.");

        while (true){
            if (node == 0 && firstTime){
                System.out.println("[Node " + node + "] Sending the first number");
                firstTime = false;
            }
            else {
                if (firstTime){
                    System.out.println("[Node " + node + "] Waiting for the first number..");
                    firstTime = false;
                }
                token = input.readLong();
            }

            token++;
            System.out.println("[Node " + node + "] " + token);
            output.writeLong(token);
            output.flush();
        }
    }
}