package eina.unizar.freshtech.adapter;

public class ElementoItem {

    private int icono;
    private String nombre;
    private String categoria;

    public ElementoItem(int icono, String nombre, String categoria) {
        this.icono = icono;
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
