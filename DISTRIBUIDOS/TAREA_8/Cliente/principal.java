//principal.java
public class principal{
    public static void main(String[] args) {
        Cliente cliente = new Cliente();

        while(true) {
            char e = cliente.mostrarMenu();
            cliente.opcion(e);
        }
    }
}

