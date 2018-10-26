package bochassportclub.bsc.Entities.API_Entities;

public class ClaseParticularApi {
    private String profesor;
    private String docProfesor;
    private int idClase;
    private String fechaReserva;
    private String horaDesde;
    private String horaHasta;
    private int idCancha;
    private String nombreCancha;
    private int nroCancha;
    private String fechaC;

    public ClaseParticularApi() {
    }

    public String getFechaC() {
        return fechaC;
    }

    public void setFechaC(String fechaC) {
        this.fechaC = fechaC;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getDocProfesor() {
        return docProfesor;
    }

    public void setDocProfesor(String docProfesor) {
        this.docProfesor = docProfesor;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
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

    public int getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(int idCancha) {
        this.idCancha = idCancha;
    }

    public String getNombreCancha() {
        return nombreCancha;
    }

    public void setNombreCancha(String nombreCancha) {
        this.nombreCancha = nombreCancha;
    }

    public int getNroCancha() {
        return nroCancha;
    }

    public void setNroCancha(int nroCancha) {
        this.nroCancha = nroCancha;
    }
}
