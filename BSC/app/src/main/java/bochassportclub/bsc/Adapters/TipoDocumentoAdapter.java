package bochassportclub.bsc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import bochassportclub.bsc.Entities.TipoDocumento;
import bochassportclub.bsc.R;

public class TipoDocumentoAdapter extends ArrayAdapter<TipoDocumento> {
    private Context context;
    List<TipoDocumento> list;

    public TipoDocumentoAdapter(Context context, List<TipoDocumento> list) {
        super(context, R.layout.spinner_simple_list_item, list);
        this.context = context;
        this.list = list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_simple_list_item, null);

        ((TextView) convertView.findViewById(R.id.texto)).setText(list.get(position).getNombre());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.spinner_simple_list_item, parent, false);
        }

        if (row.getTag() == null) {
            tipoDocHolder tipoDocHolder = new tipoDocHolder();
            tipoDocHolder.setTextView((TextView) row.findViewById(R.id.texto));
            row.setTag(tipoDocHolder);
        }

        TipoDocumento td = list.get(position);
        ((tipoDocHolder) row.getTag()).getTextView().setText(td.getNombre());

        return row;
    }

    private static class tipoDocHolder {
        private TextView textView;

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }
}
