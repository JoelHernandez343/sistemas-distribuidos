import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Exercise {

    public static String recoverResponse(BufferedReader r) throws IOException {
        String response = "", aux;
        while ((aux = r.readLine()) != null)
            response += aux;

        return response;
    }

    public static String fetch(String params, String address)
            throws MalformedURLException, IOException, RuntimeException {

        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        OutputStream sender = conn.getOutputStream();

        sender.write(params.getBytes());
        sender.flush();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            BufferedReader error = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            return recoverResponse(error);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = recoverResponse(reader);

        return response;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(fetch("a=1000", "http://sisdis.sytes.net:8080/Servicio/rest/ws/prueba"));
    }

}