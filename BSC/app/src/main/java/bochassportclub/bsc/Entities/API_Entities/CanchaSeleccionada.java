package bochassportclub.bsc.Entities.API_Entities;

public class CanchaSeleccionada extends CanchaApi {
    private boolean isSelected = false;

    public CanchaSeleccionada(CanchaApi c, boolean isSelected) {
        super(c.getId(), c.getNumero(), c.getNombre(), c.getDescripcion());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
