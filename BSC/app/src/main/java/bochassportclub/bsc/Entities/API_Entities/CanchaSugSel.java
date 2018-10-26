package bochassportclub.bsc.Entities.API_Entities;

public class CanchaSugSel extends CanchaSug {
    private boolean isSelected = false;

    public CanchaSugSel(CanchaSug c, boolean isSelected) {
        super(c.getId(), c.getNumero(), c.getNombre(), c.getDescripcion(),c.getHoradesde(),c.getHorahasta());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
