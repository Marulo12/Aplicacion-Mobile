package bochassportclub.bsc.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import bochassportclub.bsc.Entities.Noticias;
import bochassportclub.bsc.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.ViewHolder> {

    private onClickListener listener;
    private List<Noticias> list;

    public NoticiasAdapter(List<Noticias> list, onClickListener listener) {
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvnoticias, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtTitulo)
        TextView txtTitulo;
        @BindView(R.id.imgNoticias)
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Noticias n, final onClickListener listener) {
            txtTitulo.setText(n.getTitulo());
            //Picasso.get().load(n.getUrl()).error(R.drawable.ic_image_off).placeholder(R.drawable.ic_loading).into(img);
            Picasso.get().load(n.getUrl()).into(img);

            // carga de imagen

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(list.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }

    public interface onClickListener {
        void onItemClickListener(Noticias n, int position);
    }
}
