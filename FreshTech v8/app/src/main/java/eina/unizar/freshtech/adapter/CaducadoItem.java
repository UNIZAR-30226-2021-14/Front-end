package eina.unizar.freshtech.adapter;

public class CaducadoItem {

    private int icono;
    private String nombre;

    public CaducadoItem(int icono, String nombre) {
        this.icono = icono;
        this.nombre = nombre;
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
}
