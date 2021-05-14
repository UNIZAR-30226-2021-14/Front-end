package eina.unizar.freshtech.adapter;

public class CaducadoItem {

    private int icono;
    private String nombre;
    private String fechaExpiracion;

    public CaducadoItem(int icono, String nombre, String fechaExpiracion) {
        this.icono = icono;
        this.nombre = nombre;
        this.fechaExpiracion = fechaExpiracion;
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

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}
