//Ayuda.java
import java.io.*;
import java.net.*;

public class Ayuda {
    public Ayuda() {}
    private final static String URL_MAQUINA = "http://104.215.100.225:8080/Servicio/rest/ws/";
    private final static String REQUEST_KEY = "Content-Type";
    private final static String VALUE_KEY = "application/x-www-form-urlencoded";

    static Modelo hacerConsulta(String cuerpo, String metodo, String endpoint, String parametro) {
        try {
            URL url = new URL(URL_MAQUINA + endpoint);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

            conexion.setDoOutput(true);
            conexion.setRequestMethod(metodo);
            conexion.setRequestProperty(REQUEST_KEY, VALUE_KEY);

            if(cuerpo.length() > 0 && parametro.length() > 0) {
                String parametros = parametro + "=" + URLEncoder.encode(cuerpo, "UTF-8");
                OutputStream os = conexion.getOutputStream();
                os.write(parametros.getBytes(), 0, parametros.getBytes().length);
                os.flush();
                os.close();
            }

            if(conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
                return new Modelo(400, "{message: 'No encontrado'}");

            BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String respuestaServidor;
            String respuesta ="";
            while((respuestaServidor = br.readLine()) != null) {
            //System.out.println(respuestaServidor);
            respuesta += respuestaServidor;
            //System.out.println(respuesta);
            }
            System.out.println(conexion.getResponseCode());
            conexion.disconnect();
            return new Modelo(conexion.getResponseCode(),respuesta);
        } catch(Exception e) { e.printStackTrace(); }

        return new Modelo(404, "No encontrado");
    }
}
