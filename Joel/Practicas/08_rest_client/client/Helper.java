import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Helper {
    public static <T> T deserializeJson(String json, Type t) {
        Gson gson = new Gson();

        return gson.fromJson(json, t);
    }

    public static <T> String serializeJson(T object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(object);
    }

    public static String recoverResponse(BufferedReader r) throws IOException {
        String response = "", aux;
        while ((aux = r.readLine()) != null)
            response += aux;

        return response;
    }

    public static String fetch(String operation, String paramName, String args, String ip)
            throws MalformedURLException, IOException, RuntimeException {

        URL url = new URL("http://" + ip + ":8080/Servicio/rest/ws/" + operation);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String params = paramName + "=" + URLEncoder.encode(args, "UTF-8");

        OutputStream sender = conn.getOutputStream();

        sender.write(params.getBytes());
        sender.flush();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            BufferedReader error = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            return recoverResponse(error);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = recoverResponse(reader);

        return response.equals("") ? "OK" : response;
    }
}
