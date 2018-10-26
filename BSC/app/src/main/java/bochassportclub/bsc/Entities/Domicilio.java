package bochassportclub.bsc.Entities;

public class Domicilio {
    private int Id;
    private int IdBarrio;
    private int Numero;
    private String Calle;
    private int IdLocalidad;
    private String departamento;
    private int piso;

    public Domicilio() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdBarrio() {
        return IdBarrio;
    }

    public void setIdBarrio(int idBarrio) {
        IdBarrio = idBarrio;
    }

    public int getNumero() {
        return Numero;
    }

    public void setNumero(int numero) {
        Numero = numero;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String calle) {
        Calle = calle;
    }

    public int getIdLocalidad() {
        return IdLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        IdLocalidad = idLocalidad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }
}
