package bochassportclub.bsc.Presenter;

import java.util.List;

import bochassportclub.bsc.Entities.Barrio;
import bochassportclub.bsc.Entities.Localidad;
import bochassportclub.bsc.Entities.TipoDocumento;

public interface MiPerfilPresenter {

    List<Localidad> getLocalidades();

    List<Barrio> getBarrios(int idLocalidad);

    List<TipoDocumento> getTipoDocumentos();
}
