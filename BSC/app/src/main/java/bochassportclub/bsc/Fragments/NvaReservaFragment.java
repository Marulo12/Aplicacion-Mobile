package bochassportclub.bsc.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bochassportclub.bsc.Adapters.CanchasAdapter;
import bochassportclub.bsc.Adapters.CanchasSugAdapter;
import bochassportclub.bsc.Adapters.HorariosSpinnerAdapter;
import bochassportclub.bsc.Adapters.canchaSelViewHolder;
import bochassportclub.bsc.Adapters.canchaSugSelViewHolder;
import bochassportclub.bsc.Entities.API_Entities.CanchaApi;
import bochassportclub.bsc.Entities.API_Entities.CanchaSeleccionada;
import bochassportclub.bsc.Entities.API_Entities.CanchaSug;
import bochassportclub.bsc.Entities.API_Entities.CanchaSugSel;
import bochassportclub.bsc.Entities.API_Entities.VolleySingleton;
import bochassportclub.bsc.Entities.Constants;
import bochassportclub.bsc.Entities.Horarios;
import bochassportclub.bsc.R;
import bochassportclub.bsc.Util.Util;
import bochassportclub.bsc.recyclerview.adapters.AnimationAdapter;
import bochassportclub.bsc.recyclerview.adapters.ScaleInAnimationAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NvaReservaFragment extends Fragment implements canchaSelViewHolder.OnItemSelectedListener, canchaSugSelViewHolder.OnItemSugSelectedListener {

    //region Bind
    @BindView(R.id.etFecha)
    EditText etFecha;
    @BindView(R.id.spDesde)
    Spinner spDesde;
    @BindView(R.id.spHasta)
    Spinner spHasta;
    @BindView(R.id.rvCanchas)
    RecyclerView rvCanchas;
    @BindView(R.id.rvCanchasSug)
    RecyclerView rvCanchasSug;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private int dia, mes, anio;
    StringRequest stringRequest;
    private Unbinder unbinder;
    List<Horarios> list;
    SharedPreferences pref;
    String usuarioLog;
    //endregion

    public NvaReservaFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nva_reserva, container, false);
        unbinder = ButterKnife.bind(this, view);
        pref = getContext().getSharedPreferences(Constants.SPREF, Context.MODE_PRIVATE);
        usuarioLog = Util.getUsuario(pref);
        list = Util.getHorarios();
        spDesde.setAdapter(new HorariosSpinnerAdapter(getActivity(), list));
        spHasta.setAdapter(new HorariosSpinnerAdapter(getActivity(), list));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Nueva Reserva");
    }

    @OnClick(R.id.fab)
    public void guardarReserva() {

        Horarios desde = (Horarios) spDesde.getSelectedItem();
        Horarios hasta = (Horarios) spHasta.getSelectedItem();

        if (validarHorarios(desde, hasta)) {
            List<CanchaApi> list = CanchasAdapter.getCanchasSeleccionadas();
            List<CanchaSug> sugerencias = CanchasSugAdapter.getCanchasSugSeleccionadas();
            ArrayList<Integer> canchas = new ArrayList<>();

            if (list.size() == 0 && sugerencias.size() == 0) {
                Toast.makeText(getContext(), "Selecciona una cancha para registrar una reserva", Toast.LENGTH_SHORT).show();
            } else if (list.size() != 0) {
                for (CanchaApi c : list) {
                    canchas.add(c.getId());
                }
                registrarReserva(etFecha.getText().toString(), desde.getHorario(), hasta.getHorario(), canchas, usuarioLog);
            } else if (sugerencias.size() != 0) {
                CanchaSug canchaSug = sugerencias.get(0);
                canchas.add(canchaSug.getId());
                registrarReserva(etFecha.getText().toString(), canchaSug.getHoradesde().substring(0,5), canchaSug.getHorahasta().substring(0,5), canchas, usuarioLog);
            }
        } else {
            loadCanchas(new ArrayList<CanchaApi>());
            loadSugerencias(new ArrayList<CanchaSug>());
        }
    }

    @OnClick(R.id.btnComprobarDisponibilidad)
    public void comprobarDisponibilidad() {
        fab.setVisibility(View.GONE);
        loadSugerencias(new ArrayList<CanchaSug>());
        loadCanchas(new ArrayList<CanchaApi>());

        if (TextUtils.isEmpty(etFecha.getText())) {
            etFecha.setError("Ingrese una fecha");
        } else {
            Horarios desde = (Horarios) spDesde.getSelectedItem();
            Horarios hasta = (Horarios) spHasta.getSelectedItem();

            if (validarHorarios(desde, hasta)) {
                GetDisponibilidad(etFecha.getText().toString(), desde.getHorario(), hasta.getHorario());
            }
//            else {
//                ArrayList<CanchaApi> canchaApis = new ArrayList<>();
//                ArrayList<CanchaSug> canchaSug = new ArrayList<>();
//                loadCanchas(canchaApis);
//                loadSugerencias(canchaSug);
//            }
        }
    }

    private Boolean validarHorarios(Horarios desde, Horarios hasta) {
        Boolean ret = false;

        if (desde.getId() == 0) {
            Toast.makeText(getContext(), "Seleccione Hora Desde", Toast.LENGTH_LONG).show();
        } else if (hasta.getId() == 0) {
            Toast.makeText(getContext(), "Seleccione Hora Hasta", Toast.LENGTH_LONG).show();
        } else if (desde.getId() >= hasta.getId()) {
            Toast.makeText(getContext(), "La hora desde no puede ser menor o igual a la hora hasta", Toast.LENGTH_LONG).show();
        } else if (hasta.getId() - desde.getId() < 2) {
            Toast.makeText(getContext(), "Los turnos deben tener una duración mínima de una hora", Toast.LENGTH_LONG).show();
        } else {
            ret = true;
        }
        return ret;
    }

    public void loadSugerencias(ArrayList<CanchaSug> sugerencias) {
        RecyclerView.LayoutManager rvLayoutM;

        rvLayoutM = new LinearLayoutManager(getActivity());

        RecyclerView.Adapter rvAdapter = getScaleInAnimationSugerencias(this, sugerencias, false);
        rvCanchasSug.setLayoutManager(rvLayoutM);
        rvCanchasSug.setAdapter(rvAdapter);

        if (sugerencias.size() == 0) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }

    public void loadCanchas(ArrayList<CanchaApi> canchas) {
        RecyclerView.LayoutManager rvLayoutM;

        rvLayoutM = new LinearLayoutManager(getActivity());

        RecyclerView.Adapter rvAdapter = getScaleInAnimation(this, canchas, true);
        rvCanchas.setLayoutManager(rvLayoutM);
        rvCanchas.setAdapter(rvAdapter);

        if (canchas.size() == 0) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.etFecha)
    public void mostrarCalendar() {
        final Calendar cal = Calendar.getInstance();
        dia = cal.get(Calendar.DAY_OF_MONTH);
        mes = cal.get(Calendar.MONTH);
        anio = cal.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etFecha.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }
        }, anio, mes, dia);

        datePickerDialog.show();
    }

    public AnimationAdapter getScaleInAnimation(canchaSelViewHolder.OnItemSelectedListener listener,
                                                List<CanchaApi> ls, boolean isMultiSelectionEnabled) {
        CanchasAdapter adapter = new CanchasAdapter(listener, ls, isMultiSelectionEnabled);
        return new ScaleInAnimationAdapter(adapter);
    }

    public AnimationAdapter getScaleInAnimationSugerencias(canchaSugSelViewHolder.OnItemSugSelectedListener listener,
                                                           List<CanchaSug> ls, boolean isMultiSelectionEnabled) {
        CanchasSugAdapter adapter = new CanchasSugAdapter(listener, ls, isMultiSelectionEnabled);
        return new ScaleInAnimationAdapter(adapter);
    }

    @Override
    public void onItemSelected(CanchaSeleccionada cs) {
        // Seleccion de canchas
    }

    @Override
    public void onItemSelected(CanchaSugSel cs) {
        // Seleccion de Sugerencias
    }

    private void GetDisponibilidad(final String fecha, final String desde, final String hasta) {

        String url = Constants.ALQUILER_CANCHAS + "ComprobarDisponibilidad/" + fecha + "/" + desde + "/" + hasta;

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<ArrayList<CanchaApi>>() {
                }.getType();
                ArrayList<CanchaApi> canchaApis = new Gson().fromJson(response, listType);
                loadCanchas(canchaApis);
                // limpiar sugerencias ???????????????????????
                if (canchaApis.size() == 0) {
                    Toast.makeText(getContext(), "Todas las canchas se encuentran ocupadas para ese horario, Te sugerimos las siguientes opciones", Toast.LENGTH_LONG).show();
                    GetSugerencias(fecha, desde);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void GetSugerencias(String fecha, String desde) {

        String url = Constants.ALQUILER_CANCHAS + "sugerirCanchas/" + fecha + "/" + desde;

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<ArrayList<CanchaSug>>() {
                }.getType();
                ArrayList<CanchaSug> CanchaSugeridas = new Gson().fromJson(response, listType);
                loadSugerencias(CanchaSugeridas);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void registrarReserva(String fecha, String desde, String hasta, List<Integer> canchas, String usuario) {
        String url = Constants.ALQUILER_CANCHAS + "RegistrarReserva";

        JSONObject jsonParams = new JSONObject();

        JSONArray array = new JSONArray();

        for (int i = 0; i < canchas.size(); i++) {
            array.put(canchas.get(i));
        }

        try {
            jsonParams.put("fecR", fecha);
            jsonParams.put("hd", desde);
            jsonParams.put("hh", hasta);
            jsonParams.put("Canchas", array);
            jsonParams.put("Usuario", usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("statusCode").equals("200")) {
                                Toast.makeText(getContext(), "Su reserva fue registrada exitosamente", Toast.LENGTH_SHORT).show();
                                etFecha.setText("");
                                spDesde.setSelection(0);
                                spHasta.setSelection(0);
                                loadCanchas(new ArrayList<CanchaApi>());
                                loadSugerencias(new ArrayList<CanchaSug>());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Ha ocurrido un error, intente luego", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        }) {
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
    }
}