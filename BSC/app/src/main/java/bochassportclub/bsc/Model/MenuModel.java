package bochassportclub.bsc.Model;

import bochassportclub.bsc.Datos.bd;
import bochassportclub.bsc.Entities.Jugador;
import bochassportclub.bsc.Presenter.MenuPresenter;
import bochassportclub.bsc.View.MenuView;

public class MenuModel implements MenuPresenter {
    MenuView menuView;

    public MenuModel(MenuView menuView) {
        this.menuView = menuView;
    }

    @Override
    public boolean logOut(int idJugador) {
        //return bd.cerrarSesion(idJugador);
        return false;
    }

    @Override
    public Jugador getJugador(int idJugador) {
        return null;
    }
}
