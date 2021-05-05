package eina.unizar.freshtech.model;

public class ElementoDetalladoImagen {

    private String nombreImagen;
    private String categoria;
    private String fechacreacion;
    private String fechacaducidad;

    public ElementoDetalladoImagen(String nombreImagen, String categoria, String fechacreacion, String fechacaducidad) {
        this.nombreImagen = nombreImagen;
        this.categoria = categoria;
        this.fechacreacion = fechacreacion;
        this.fechacaducidad = fechacaducidad;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
}