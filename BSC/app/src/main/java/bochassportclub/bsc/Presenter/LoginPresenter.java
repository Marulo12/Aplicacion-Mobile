package bochassportclub.bsc.Presenter;

import java.util.List;

import bochassportclub.bsc.Entities.Barrio;
import bochassportclub.bsc.Entities.Domicilio;
import bochassportclub.bsc.Entities.Localidad;
import bochassportclub.bsc.Entities.Persona;
import bochassportclub.bsc.Entities.TipoDocumento;
import bochassportclub.bsc.Entities.Usuario;

public interface LoginPresenter {

    void performLogin(String user, String pass);

    void performSignUp(Domicilio domicilio, Usuario usuario, Persona persona);

    void performGetPass(String email);

    List<Localidad> getLocalidades();

    List<Barrio> getBarrios(int idLocalidad);

    List<TipoDocumento> getTipoDocumentos();

    void registrarInicioSession(int idJugador);
}
