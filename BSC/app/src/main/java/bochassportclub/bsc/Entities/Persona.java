package bochassportclub.bsc.Entities;

import java.util.Date;

public class Persona {
    //    @SerializedName("")
    private int Id;
    private String NroDocumento;
    private int IdTipoDocumento;
    private int IdDomicilio;
    private int IdUsuario;
    private String Nombre;
    private String Apellido;
    private Date FechaBaja;
    private String Mail;
    private String Telefono;
    private String Tipo;

    public Persona() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNroDocumento() {
        return NroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        NroDocumento = nroDocumento;
    }

    public int getIdTipoDocumento() {
        return IdTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        IdTipoDocumento = idTipoDocumento;
    }

    public int getIdDomicilio() {
        return IdDomicilio;
    }

    public void setIdDomicilio(int idDomicilio) {
        IdDomicilio = idDomicilio;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public Date getFechaBaja() {
        return FechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        FechaBaja = fechaBaja;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }
}
