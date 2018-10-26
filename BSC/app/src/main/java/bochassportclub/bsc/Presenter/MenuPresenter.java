package bochassportclub.bsc.Presenter;

import bochassportclub.bsc.Entities.Jugador;

public interface MenuPresenter {
    boolean logOut(int idJugador);
    Jugador getJugador(int idJugador);
}
