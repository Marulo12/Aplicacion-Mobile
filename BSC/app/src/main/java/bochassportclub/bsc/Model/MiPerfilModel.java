package bochassportclub.bsc.Model;

import java.util.List;

import bochassportclub.bsc.Datos.bd;
import bochassportclub.bsc.Entities.Barrio;
import bochassportclub.bsc.Entities.Localidad;
import bochassportclub.bsc.Entities.TipoDocumento;
import bochassportclub.bsc.Presenter.MiPerfilPresenter;
import bochassportclub.bsc.View.MiPerfilView;

public class MiPerfilModel implements MiPerfilPresenter {

    MiPerfilView miPerfilView;

    public MiPerfilModel(MiPerfilView miPerfilView) {
        this.miPerfilView = miPerfilView;
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
}
