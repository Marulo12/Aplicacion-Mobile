package bochassportclub.bsc.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bochassportclub.bsc.Entities.API_Entities.CanchaSug;
import bochassportclub.bsc.Entities.API_Entities.CanchaSugSel;
import bochassportclub.bsc.R;

public class CanchasSugAdapter extends RecyclerView.Adapter implements canchaSugSelViewHolder.OnItemSugSelectedListener {
    private static List<CanchaSugSel> lista;
    private boolean isMultiSelectionEnabled =false;
    canchaSugSelViewHolder.OnItemSugSelectedListener listener;

    public CanchasSugAdapter(canchaSugSelViewHolder.OnItemSugSelectedListener listener,
                          List<CanchaSug> ls, boolean isMultiSelectionEnabled){
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        lista = new ArrayList<>();
        for (CanchaSug comp : ls){
            lista.add(new CanchaSugSel(comp,false));
        }
    }

    @Override
    public canchaSugSelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_canchas_sug,parent,false);
        return new canchaSugSelViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {

        canchaSugSelViewHolder holder = (canchaSugSelViewHolder) viewholder;
        CanchaSugSel cs = lista.get(position);

        holder.etNumCancha.setText(cs.getId() + "");
        holder.etNombre.setText(cs.getNombre());
        holder.etDesc.setText(cs.getDescripcion());
        holder.etDesde.setText(cs.getHoradesde().substring(0,5) + " Hs");
        holder.etHasta.setText(cs.getHorahasta().substring(0,5) + " Hs");
        holder.imgCheck.setImageResource(R.drawable.ic_round);
        holder.cancha_sug_sel = cs;
        holder.setChecked(holder.cancha_sug_sel.isSelected());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static List<CanchaSug> getCanchasSugSeleccionadas(){

        List<CanchaSug> canchasSel = new ArrayList<>();
        for (CanchaSugSel item : lista){
            if (item.isSelected()){
                canchasSel.add(item);
            }
        }
        return canchasSel;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return canchaSugSelViewHolder.MULTI_SELECTION;
        }
        else{
            return canchaSugSelViewHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(CanchaSugSel item) {
        if (!isMultiSelectionEnabled) {
            for (CanchaSugSel cs : lista){
                if(!cs.equals(item) && cs.isSelected()){
                    cs.setSelected(false);
                }else if (cs.equals(item) && cs.isSelected()){
                    cs.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);
    }
}
