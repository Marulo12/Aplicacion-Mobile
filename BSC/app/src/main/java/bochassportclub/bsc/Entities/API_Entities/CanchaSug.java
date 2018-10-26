package bochassportclub.bsc.Entities.API_Entities;

public class CanchaSug {
    private int id;
    private int numero;
    private String nombre;
    private String descripcion;
    private String horadesde;
    private String horahasta;

    public CanchaSug(int id, int numero, String nombre, String descripcion, String horaDesde, String horaHasta) {
        this.id = id;
        this.numero = numero;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.horadesde = horaDesde;
        this.horahasta = horaHasta;
    }

    public CanchaSug() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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

    public String getHoradesde() {
        return horadesde;
    }

    public void setHoradesde(String horadesde) {
        this.horadesde = horadesde;
    }

    public String getHorahasta() {
        return horahasta;
    }

    public void setHorahasta(String horahasta) {
        this.horahasta = horahasta;
    }
}
