package Obejtos;

public class Servicio {
    private int id;
    private String nombrePerro;
    private  String nombreResponsable;
    private String servicio;
    private String comentarios;
    private String numTel;
    private int precio;
    private String fecha;
    private String entregado;
    public Servicio(int id, String nombrePerro, String nombreResponsable, String servicio, String comentarios, String numTel, int precio, String fecha, String entregado) {
        this.id = id;
        this.nombrePerro = nombrePerro;
        this.nombreResponsable = nombreResponsable;
        this.servicio = servicio;
        this.comentarios = comentarios;
        this.numTel = numTel;
        this.precio = precio;
        this.fecha = fecha;
        this.entregado=entregado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombrePerro() {
        return nombrePerro;
    }

    public void setNombrePerro(String nombrePerro) {
        this.nombrePerro = nombrePerro;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEntregado() {
        return entregado;
    }

    public void setEntregado(String entregado) {
        this.entregado = entregado;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", nombrePerro='" + nombrePerro + '\'' +
                ", nombreResponsable='" + nombreResponsable + '\'' +
                ", servicio='" + servicio + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", numTel='" + numTel + '\'' +
                ", precio=" + precio +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
