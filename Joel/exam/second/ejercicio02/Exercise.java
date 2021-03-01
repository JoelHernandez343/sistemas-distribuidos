import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Exercise {
    class Coordenada {
        int x;
        int y;
        int z;
    }

    public static String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        StringBuilder json = new StringBuilder();
        String aux;
        while ((aux = reader.readLine()) != null)
            json.append(aux);

        reader.close();

        return json.toString();
    }

    public static <T> T getListOfFile(File file, Type t) throws IOException {
        String json = readFile(file);
        Gson gson = new Gson();

        return gson.fromJson(json, t);
    }

    public static void main(String[] args) throws Exception {
        List<Coordenada> coordenadas = getListOfFile(new File("coordenadas.txt"), new TypeToken<List<Coordenada>>() {
        }.getType());

        long x = 0, y = 0, z = 0;

        for (int i = 0; i < coordenadas.size(); ++i) {
            x += coordenadas.get(i).x;
            y += coordenadas.get(i).y;
            z += coordenadas.get(i).z;
        }

        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
    }
}