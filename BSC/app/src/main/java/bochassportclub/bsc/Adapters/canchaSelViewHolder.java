package bochassportclub.bsc.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bochassportclub.bsc.Entities.API_Entities.CanchaSeleccionada;
import bochassportclub.bsc.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class canchaSelViewHolder extends RecyclerView.ViewHolder {

    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    CanchaSeleccionada cancha_sel;
    OnItemSelectedListener itemSelectedListener;

    // elementos del layout
    @BindView(R.id.imgCheck)
    ImageView imgCheck;
    @BindView(R.id.etNumCancha)
    TextView etNumCancha;
    @BindView(R.id.etNombre)
    TextView etNombre;
    @BindView(R.id.etDesc)
    TextView etDesc;
    @BindView(R.id.cvCancha)
    CardView cardViewCancha;

    public canchaSelViewHolder(View view, OnItemSelectedListener listener) {
        super(view);
        ButterKnife.bind(this, view);
        itemSelectedListener = listener;

        cardViewCancha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancha_sel.isSelected() && getItemViewType() == MULTI_SELECTION) {
                    setChecked(false);
                } else {
                    setChecked(true);
                }
                itemSelectedListener.onItemSelected(cancha_sel);
            }
        });
    }

    public void setChecked(boolean value) {
        if (value) {
            imgCheck.setImageResource(R.drawable.ic_right);
        } else {
            imgCheck.setImageResource(R.drawable.ic_round);
        }
        cancha_sel.setSelected(value);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(CanchaSeleccionada cs);
    }
}
