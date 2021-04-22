package eina.unizar.freshtech.model;

public class Elemento {

    private String dominio;
    private String nombre;
    private String tipo;
    private String fechacreacion;
    private String fechacaducidad;
    private String categoria;

    public Elemento(String dominio, String nombre, String tipo, String fechacreacion, String fechacaducidad, String categoria) {
        this.dominio = dominio;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fechacreacion = fechacreacion;
        this.fechacaducidad = fechacaducidad;
        this.categoria = categoria;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(String fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public String getFechacaducidad() {
        return fechacaducidad;
    }

    public void setFechacaducidad(String fechacaducidad) {
        this.fechacaducidad = fechacaducidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
