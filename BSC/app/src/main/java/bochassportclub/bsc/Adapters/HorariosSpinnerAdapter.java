package bochassportclub.bsc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import bochassportclub.bsc.Entities.Horarios;
import bochassportclub.bsc.R;

public class HorariosSpinnerAdapter extends ArrayAdapter<Horarios> {
    private Context context;
    List<Horarios> datos = null;


    public HorariosSpinnerAdapter(Context context, List<Horarios> datos) {
        super(context, R.layout.spinner_simple_list_item, datos);
        this.context = context;
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_simple_list_item, null);
        }
        ((TextView) convertView.findViewById(R.id.texto)).setText(datos.get(position).getHorario());

        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.spinner_simple_list_item, parent, false);
        }

        if (row.getTag() == null) {
            HorariosSpinnerAdapter.HorariosHolder tipoRecHolder = new HorariosSpinnerAdapter.HorariosHolder();
            tipoRecHolder.setTextView((TextView) row.findViewById(R.id.texto));
            row.setTag(tipoRecHolder);
        }

        //rellenamos el layout con los datos de la fila que se está procesando
        Horarios horario = datos.get(position);
        ((HorariosSpinnerAdapter.HorariosHolder) row.getTag()).getTextView().setText(horario.getHorario());

        return row;
    }

    private static class HorariosHolder {
        private TextView textView;

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }
}
