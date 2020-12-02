import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.reflect.TypeToken;

public class Client {

    static String ip;

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.out.println(ConsoleColors.ERR + "Ingresar como parámetro la IP.");
            System.exit(1);
        }

        ip = args[0];

        while (true) {
            System.out.println("a. Alta de usuario");
            System.out.println("b. Consulta de usuario");
            System.out.println("c. Borrar usuario");
            System.out.println("d. Borrar todos los usuarios");
            System.out.println("e. Salir");
            System.out.println("Ingresa una opción: ");

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String option = keyboard.readLine();

            if (option.equals("a")) {
                userRegistration(keyboard);
            } else if (option.equals("b")) {
                queryUser(keyboard);
            } else if (option.equals("c")) {
                deleteUser(keyboard);
            } else if (option.equals("d")) {
                deleteAllUsers();
            } else if (option.equals("e")) {
                break;
            } else {
                System.out.println("\"" + option + "\" no es una opción válida.");
            }
        }

        System.out.println("Bye!");
    }

    public static void userRegistration(BufferedReader keyboard) throws Exception {
        System.out.println("(Creación)");

        System.out.println("Email: ");
        String email = keyboard.readLine();

        System.out.println("Nombre: ");
        String name = keyboard.readLine();

        System.out.println("Apellido paterno: ");
        String fathersSurname = keyboard.readLine();

        System.out.println("Apellido materno: ");
        String mothersSurname = keyboard.readLine();

        System.out.println("Fecha de nacimiento (YYYY-MM-DD): ");
        String birth = keyboard.readLine();

        System.out.println("Teléfono: ");
        String phone = keyboard.readLine();

        System.out.println("Genero (M / F): ");
        String genre = keyboard.readLine();

        Usuario user = new Usuario(email, name, fathersSurname, mothersSurname, birth, phone, genre);

        String json = Helper.serializeJson(user);

        String response = Helper.fetch("alta", "usuario", json, ip);
        System.out.println(
                ConsoleColors.BLACK_BG + ConsoleColors.YELLOW + "RESPONSE:" + ConsoleColors.RESET + " " + response);
    }

    public static void queryUser(BufferedReader keyboard) throws Exception {
        System.out.println("(Consulta) Email: ");
        String email = keyboard.readLine();

        String response = Helper.fetch("consulta", "email", email, ip);

        try {
            Usuario queryUser = Helper.deserializeJson(response, new TypeToken<Usuario>() {
            }.getType());
            queryUser.showIt();
        } catch (Exception e) {
            System.out.println(
                    ConsoleColors.BLACK_BG + ConsoleColors.YELLOW + "RESPONSE:" + ConsoleColors.RESET + " " + response);
        }

    }

    public static void deleteUser(BufferedReader keyboard) throws Exception {
        System.out.println("(Eliminación) Email: ");
        String email = keyboard.readLine();

        String response = Helper.fetch("borra", "email", email, ip);
        System.out.println(
                ConsoleColors.BLACK_BG + ConsoleColors.YELLOW + "RESPONSE:" + ConsoleColors.RESET + " " + response);
    }

    public static void deleteAllUsers() throws Exception {
        System.out.println("(Eliminando todos)");

        String response = Helper.fetch("borrar_usuarios", "", "", ip);
        System.out.println(
                ConsoleColors.BLACK_BG + ConsoleColors.YELLOW + "RESPONSE:" + ConsoleColors.RESET + " " + response);
    }

}