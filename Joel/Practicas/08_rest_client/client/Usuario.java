public class Usuario {

    String email;
    String nombre;
    String apellido_paterno;
    String apellido_materno;
    String fecha_nacimiento;
    String telefono;
    String genero;
    byte[] foto;

    public Usuario(String email, String nombre, String apellido_paterno, String apellido_materno,
            String fecha_nacimiento, String telefono, String genero) {
        this.email = email;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.fecha_nacimiento = fecha_nacimiento;
        this.telefono = telefono;
        this.genero = genero;
        foto = null;
    }

    public void showIt() throws Exception {
        if (email == null)
            throw new Exception("Usuario no válido");

        System.out.println("Email: " + email);
        System.out.println("Nombre: " + nombre);
        System.out.println("Apellido paterno: " + apellido_paterno);
        System.out.println("Apellido materno: " + apellido_materno);
        System.out.println("Fecha de nacimiento: " + fecha_nacimiento);
        System.out.println("Teléfono: " + telefono);
        System.out.println("Género: " + genero);
    }
}
