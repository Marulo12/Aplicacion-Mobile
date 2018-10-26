package bochassportclub.bsc.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bochassportclub.bsc.Entities.API_Entities.ClaseParticularApi;
import bochassportclub.bsc.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MisClasesAdapter extends RecyclerView.Adapter<MisClasesAdapter.ViewHolder> {
    private List<ClaseParticularApi> list;
    private ItemClickListener mClickListener;

    public MisClasesAdapter(List<ClaseParticularApi> list, Context mCtx, ItemClickListener mClickListener) {
        this.list = list;
        Context mCtx1 = mCtx;
        this.mClickListener = mClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_clases_part, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MisClasesAdapter.ViewHolder holder, int position) {
        ClaseParticularApi clase = list.get(position);

        holder.txtFecha.setText(clase.getFechaReserva());
        holder.txtHoraDesde.setText(clase.getHoraDesde().substring(0,5));
        holder.txtHoraHasta.setText(clase.getHoraHasta().substring(0,5));
        holder.txtProfesor.setText(clase.getProfesor());
        holder.txtCancha.setText(clase.getNroCancha() + "");

        if (clase.getFechaC() != null){
            holder.txtEstado.setText("Cancelada");
            holder.imgEstado.setImageResource(R.drawable.ic_cancel);
        }else{
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date hoy = new Date();
            Date fecha_clase = null;
            try {
                fecha_clase = formatter.parse(clase.getFechaReserva());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (fecha_clase.before(hoy)){
                holder.txtEstado.setText("Finalizada");
            }else{
                holder.txtEstado.setText("Confirmada");
                holder.imgEstado.setImageResource(R.drawable.ic_confirm);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imgEstado)
        public ImageView imgEstado;
        @BindView(R.id.txtEstado)
        public TextView txtEstado;
        @BindView(R.id.txtFecha)
        public TextView txtFecha;
        @BindView(R.id.txtHoraDesde)
        public TextView txtHoraDesde;
        @BindView(R.id.txtHoraHasta)
        public TextView txtHoraHasta;
        @BindView(R.id.txtProfesor)
        public TextView txtProfesor;
        @BindView(R.id.txtCancha)
        public TextView txtCancha;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
