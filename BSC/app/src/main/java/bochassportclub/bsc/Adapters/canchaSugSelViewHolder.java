package bochassportclub.bsc.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bochassportclub.bsc.Entities.API_Entities.CanchaSugSel;
import bochassportclub.bsc.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class canchaSugSelViewHolder extends RecyclerView.ViewHolder {
    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    CanchaSugSel cancha_sug_sel;
    OnItemSugSelectedListener itemSelectedListener;

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
    @BindView(R.id.etDesde)
    TextView etDesde;
    @BindView(R.id.etHasta)
    TextView etHasta;

    public canchaSugSelViewHolder(View view, OnItemSugSelectedListener listener) {
        super(view);
        ButterKnife.bind(this, view);
        itemSelectedListener = listener;

        cardViewCancha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancha_sug_sel.isSelected() && getItemViewType() == MULTI_SELECTION) {
                    setChecked(false);
                } else {
                    setChecked(true);
                }
                itemSelectedListener.onItemSelected(cancha_sug_sel);
            }
        });
    }

    public void setChecked(boolean value) {
        if (value) {
            imgCheck.setImageResource(R.drawable.ic_right);
        } else {
            imgCheck.setImageResource(R.drawable.ic_round);
        }
        cancha_sug_sel.setSelected(value);
    }

    public interface OnItemSugSelectedListener {
        void onItemSelected(CanchaSugSel cs);
    }
}
