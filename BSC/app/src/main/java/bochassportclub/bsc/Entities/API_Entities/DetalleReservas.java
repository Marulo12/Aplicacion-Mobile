package bochassportclub.bsc.Entities.API_Entities;

public class DetalleReservas {

    private int idCancha;
    private int numCancha;
    private String nombre;
    private String descripcion;
    private String horaDesde;
    private String horaHasta;
    private String estado;
    private int idEstadoDetalleReserva;

    public DetalleReservas() {
    }

    public int getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(int idCancha) {
        this.idCancha = idCancha;
    }

    public int getNumCancha() {
        return numCancha;
    }

    public void setNumCancha(int numCancha) {
        this.numCancha = numCancha;
    }

    public int getIdEstadoDetalleReserva() {
        return idEstadoDetalleReserva;
    }

    public void setIdEstadoDetalleReserva(int idEstadoDetalleReserva) {
        this.idEstadoDetalleReserva = idEstadoDetalleReserva;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHoraDesde() {
        return horaDesde;
    }

    public void setHoraDesde(String horaDesde) {
        this.horaDesde = horaDesde;
    }

    public String getHoraHasta() {
        return horaHasta;
    }

    public void setHoraHasta(String horaHasta) {
        this.horaHasta = horaHasta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
