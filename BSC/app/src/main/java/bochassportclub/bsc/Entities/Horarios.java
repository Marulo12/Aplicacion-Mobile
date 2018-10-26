package bochassportclub.bsc.Entities;

public class Horarios {
    int id;
    String horario;

    public Horarios() {
    }

    public Horarios(int id, String horario) {
        this.id = id;
        this.horario = horario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
