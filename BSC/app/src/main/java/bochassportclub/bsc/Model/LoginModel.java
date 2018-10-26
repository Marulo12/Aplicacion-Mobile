package bochassportclub.bsc.Model;

import java.util.List;

import bochassportclub.bsc.Datos.bd;
import bochassportclub.bsc.Entities.Barrio;
import bochassportclub.bsc.Entities.Domicilio;
import bochassportclub.bsc.Entities.Localidad;
import bochassportclub.bsc.Entities.Persona;
import bochassportclub.bsc.Entities.TipoDocumento;
import bochassportclub.bsc.Entities.Usuario;
import bochassportclub.bsc.Presenter.LoginPresenter;
import bochassportclub.bsc.Util.Util;
import bochassportclub.bsc.View.LoginView;

public class LoginModel implements LoginPresenter {

    LoginView mLoginView;

    public LoginModel(LoginView mLoginView) {
        this.mLoginView = mLoginView;
    }

    @Override
    public void performLogin(String user, String pass) {
        int idJugador = bd.iniciarSesion(user, Util.md5(pass));
        if (idJugador != 0) // consulta de usuario a la BD

            mLoginView.loginSuccess(idJugador);
        else
            mLoginView.loginError();
    }

    @Override
    public void performSignUp(Domicilio domicilio, Usuario usuario, Persona persona) {
        int idUsuario = bd.registrarJugador(domicilio, usuario, persona);

        if (idUsuario != 0)
            mLoginView.signUpSuccess(idUsuario);
        else
            mLoginView.signUpError();
    }

    @Override
    public void performGetPass(String email) {

    }

    @Override
    public List<Localidad> getLocalidades() {
        return bd.getLocalidades();
    }

    @Override
    public List<Barrio> getBarrios(int idLocalidad) {
        return bd.getBarrios(idLocalidad);
    }

    @Override
    public List<TipoDocumento> getTipoDocumentos() {
        return bd.getTipoDocumento();
    }

    @Override
    public void registrarInicioSession(int idJugador) {
        if (bd.registrarSession(idJugador))
            mLoginView.redirectMenu();
    }
}