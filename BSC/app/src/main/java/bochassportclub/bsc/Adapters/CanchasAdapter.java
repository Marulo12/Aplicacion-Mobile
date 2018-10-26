package bochassportclub.bsc.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bochassportclub.bsc.Entities.API_Entities.CanchaApi;
import bochassportclub.bsc.Entities.API_Entities.CanchaSeleccionada;
import bochassportclub.bsc.R;

public class CanchasAdapter extends RecyclerView.Adapter implements canchaSelViewHolder.OnItemSelectedListener{
    private static List<CanchaSeleccionada> lista;
    private boolean isMultiSelectionEnabled =false;
    canchaSelViewHolder.OnItemSelectedListener listener;

    public CanchasAdapter(canchaSelViewHolder.OnItemSelectedListener listener,
                       List<CanchaApi> ls, boolean isMultiSelectionEnabled){
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        lista = new ArrayList<>();
        for (CanchaApi comp : ls){
            lista.add(new CanchaSeleccionada(comp,false));
        }
    }

    @Override
    public canchaSelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_canchas,parent,false);
        return new canchaSelViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {

        canchaSelViewHolder holder = (canchaSelViewHolder) viewholder;
        CanchaSeleccionada cs = lista.get(position);

        holder.etNumCancha.setText(cs.getId() + "");
        holder.etNombre.setText(cs.getNombre());
        holder.etDesc.setText(cs.getDescripcion());

        // carga el comprobante seleccionado
        // es posible cambiar imagen o forma de identificacion de seleccionado o no

        holder.imgCheck.setImageResource(R.drawable.ic_round);
        holder.cancha_sel = cs;
        holder.setChecked(holder.cancha_sel.isSelected());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static List<CanchaApi> getCanchasSeleccionadas(){

        List<CanchaApi> canchasSel = new ArrayList<>();
        for (CanchaSeleccionada item : lista){
            if (item.isSelected()){
                canchasSel.add(item);
            }
        }
        return canchasSel;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return canchaSelViewHolder.MULTI_SELECTION;
        }
        else{
            return canchaSelViewHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(CanchaSeleccionada item) {
        if (!isMultiSelectionEnabled) {
            for (CanchaSeleccionada cs : lista){
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
