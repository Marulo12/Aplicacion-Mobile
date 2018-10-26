package bochassportclub.bsc.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bochassportclub.bsc.Entities.API_Entities.ReservasApi;
import bochassportclub.bsc.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MisReservasAdapter extends RecyclerView.Adapter<MisReservasAdapter.ViewHolder> {

    private List<ReservasApi> list;
    private ItemClickListener mClickListener;


    public MisReservasAdapter(List<ReservasApi> list, Context mCtx,ItemClickListener mClickListener) {
        this.list = list;
        Context mCtx1 = mCtx;
        this.mClickListener = mClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_reservas, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MisReservasAdapter.ViewHolder holder, int position) {
        ReservasApi myList = list.get(position);

        holder.txtEstado.setText(myList.getEstado());

        int idEstado = myList.getIdEstado();

        if(idEstado==2){
            holder.imgEstado.setImageResource(R.drawable.ic_confirm);
        }else if(idEstado == 5){
            holder.imgEstado.setImageResource(R.drawable.ic_cancel);
        }

        holder.txtFecha.setText(myList.getFechaReserva());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txtEstado)
        public TextView txtEstado;
        @BindView(R.id.txtFecha)
        public TextView txtFecha;
        @BindView(R.id.txtOptions)
        public TextView buttonViewOption;
        @BindView(R.id.imgEstado)
        public ImageView imgEstado;

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