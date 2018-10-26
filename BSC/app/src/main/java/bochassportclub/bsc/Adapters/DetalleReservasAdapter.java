package bochassportclub.bsc.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bochassportclub.bsc.Entities.API_Entities.DetalleReservas;
import bochassportclub.bsc.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetalleReservasAdapter extends RecyclerView.Adapter<DetalleReservasAdapter.ViewHolder> {

    private List<DetalleReservas> list;
    private int layout;
    private OnItemClickListener listener;

    public DetalleReservasAdapter(List<DetalleReservas> list, int layout, OnItemClickListener listener) {
        this.list = list;
        this.layout = layout;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position),listener);
    }

    @Override
    public int getItemCount() {return list.size();}

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txtEstado)
        public TextView txtEstado;
        @BindView(R.id.txtHoraDesde)
        public TextView txtHoraDesde;
        @BindView(R.id.txtHoraHasta)
        public TextView txtHoraHasta;
        @BindView(R.id.imgEstado)
        public ImageView imgEstado;
        @BindView(R.id.txtCancha)
        public TextView txtCancha;
//        @BindView(R.id.txtOptions)
//        public TextView buttonViewOption;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(DetalleReservas dr, final OnItemClickListener listener){
            txtHoraDesde.setText(dr.getHoraDesde().substring(0,5));
            txtHoraHasta.setText(dr.getHoraHasta().substring(0,5));
            txtEstado.setText(dr.getEstado());
            txtCancha.setText(dr.getNumCancha() + "");

            int idEstado = dr.getIdEstadoDetalleReserva();

            if(idEstado==2){
                imgEstado.setImageResource(R.drawable.ic_confirm);
            }else if(idEstado == 5){
                imgEstado.setImageResource(R.drawable.ic_cancel);
            }

//            txtProb.setText(pr.getProblema());
//            txtTipoProb.setText(pr.getTipo());
//            txtSubTipo.setText(pr.getSubtipo());
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onItemClickListener(list.get(getAdapterPosition()),getAdapterPosition());
//                }
//            });

        }
    }


    public interface OnItemClickListener{
        void onItemClickListener(DetalleReservas detalleReserva, int position);
    }
}
