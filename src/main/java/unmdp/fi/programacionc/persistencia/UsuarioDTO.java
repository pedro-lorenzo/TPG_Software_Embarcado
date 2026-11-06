package unmdp.fi.programacionc.persistencia;

public class UsuarioDTO {

    private final String nombreUsuario;
    private final String contrasenia;

    public UsuarioDTO(String nombreUsuario, String contrasenia) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public String getContrasenia()   { return contrasenia; }
}