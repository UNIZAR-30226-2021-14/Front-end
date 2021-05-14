package eina.unizar.freshtech.model;

public class ElementoDetallado {

    private String concreteuser;
    private String concretpasswd;
    private String dominio;
    private String categoria;
    private String fechacreacion;
    private String fechacaducidad;

    public ElementoDetallado(String concreteuser, String concretepasswd, String dominio, String categoria, String fechacreacion, String fechacaducidad) {
        this.concreteuser = concreteuser;
        this.concretpasswd = concretepasswd;
        this.dominio = dominio;
        this.categoria = categoria;
        this.fechacreacion = fechacreacion;
        this.fechacaducidad = fechacaducidad;
    }

    public String getConcreteuser() {
        return concreteuser;
    }

    public void setConcreteuser(String concreteuser) {
        this.concreteuser = concreteuser;
    }

    public String getConcretpasswd() {
        return concretpasswd;
    }

    public void setConcretpasswd(String concretpasswd) {
        this.concretpasswd = concretpasswd;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
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
