package eina.unizar.freshtech.model;

public class Categoria {

    private String nombrecat;

    public Categoria(String nombre) {
        this.nombrecat = nombre;
    }

    public String getNombre() {
        return nombrecat;
    }

    public void setNombre(String nombre) {
        this.nombrecat = nombre;
    }
}
