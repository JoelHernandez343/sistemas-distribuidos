//principal.java
public class Principal {
    public static void main(String[] args) {
        Cliente cliente = new cliente();

        while(true) {
            char e = cliente.mostrarMenu();
            cliente.opcion(e);
        }
    }
}

