package bochassportclub.bsc.Entities.API_Entities;

public class CanchaApi {
    private int id;
    private int numero;
    private String nombre;
    private String descripcion;

    public CanchaApi(int id, int numero, String nombre, String descripcion) {
        this.id = id;
        this.numero = numero;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public CanchaApi() {
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
}
