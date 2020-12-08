//Cliente.java
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.Scanner;

public class Cliente {
    public Cliente(){}
    
    
    

    protected char mostrarMenu(){
        Scanner s=new Scanner(System.in);
        System.out.println("Que accion desea realizar");
        System.out.println("a. Alta a un usuario");
        System.out.println("b. Consultar a un usuario");
        System.out.println("c. Borra a un usuario");
        System.out.println("d. Borra a todos los usuarios");
        System.out.println("e. Salir");

        char seleccion = s.nextLine().charAt(0);

        return seleccion;
    }
    protected void opcion(char opcion){
        switch (opcion){
            case 'a':
                AltaUsuario();
                break;
            case 'b':
                ConsultarUsuario();
                break;
            case 'c':
                BorrarUsuario();
                break;
            case 'd':
                BorrarTodo();
                break;
            case 'e':
                System.exit(0);
                break;
            default:
                System.out.println("Vuelve a escoger a otra opcion");
                break;
        }
    }
    private void AltaUsuario() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();

        Gson gson = builder.create();

        try {
            Usuario usuario = new Usuario();
            usuario = UsuarioUtils.crearUsuario(usuario);
            String cuerpo = gson.toJson(usuario);
            ResponseModel response = GenericServices.hacerConsulta(cuerpo, POST_METHOD, "alta", "usuario");
            if(response.getResponseCode() != 400) {
                System.out.println(response.getMessage());
            } else {
                Error error = gson.fromJson(response.getMessage(), Error.class);
                System.out.println(error.message);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void ConsultarUsuario() {
        Gson gson = new Gson();

        try {
            String cuerpo = UsuarioUtils.leerEmail();
            ResponseModel response = GenericServices.hacerConsulta(cuerpo, GET_METHOD, "consulta", "email");

            if(response.getResponseCode() != 400) {
                Usuario usuario = gson.fromJson(response.getMessage(), Usuario.class);
                System.out.println(usuario.toString());
            } else {
                Error error = gson.fromJson(response.getMessage(), Error.class);
                System.out.println(error.message);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void BorrarUsuario() {
        Gson gson = new Gson();

        try {
            String cuerpo = UsuarioUtils.leerEmail();
            ResponseModel response = GenericServices.hacerConsulta(cuerpo, POST_METHOD, "borra", "email");

            if(response.getResponseCode() != 400) {
                System.out.println(response.getMessage());
            } else {
                Error error = gson.fromJson(response.getMessage(), Error.class);
                System.out.println(error.message);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void BorrarTodos() {
        Gson gson = new Gson();

        try {
            ResponseModel response = GenericServices.hacerConsulta("", POST_METHOD, "borrar", "");
            if(response.getResponseCode() != 400) {
                System.out.println(response.getMessage());
            } else {
                Error error = gson.fromJson(response.getMessage(), Error.class);
                System.out.println(error.message);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    private final String POST_METHOD = "POST";
    private final String GET_METHOD = "GET";
}
}

