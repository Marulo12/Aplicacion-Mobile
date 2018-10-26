package bochassportclub.bsc.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import bochassportclub.bsc.Adapters.DetalleReservasAdapter;
import bochassportclub.bsc.Adapters.DiasSpinnerAdapter;
import bochassportclub.bsc.Adapters.MisReservasAdapter;
import bochassportclub.bsc.Entities.API_Entities.DetalleReservas;
import bochassportclub.bsc.Entities.API_Entities.ReservasApi;
import bochassportclub.bsc.Entities.API_Entities.VolleySingleton;
import bochassportclub.bsc.Entities.Constants;
import bochassportclub.bsc.Entities.Dias;
import bochassportclub.bsc.R;
import bochassportclub.bsc.Util.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MisReservasFragment extends Fragment {

    @BindView(R.id.rvMisReservas)
    RecyclerView recyclerView;
    @BindView(R.id.spDias)
    Spinner spDias;
    List<Dias> list;
    private Unbinder unbinder;
    Dialog customDialog = null;
    StringRequest stringRequest;
    SharedPreferences pref;
    String usuarioLog;

    private RecyclerView.Adapter adapter;

    public MisReservasFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mis Reservas");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mis_reservas, container, false);
        unbinder = ButterKnife.bind(this, view);
        pref = getContext().getSharedPreferences(Constants.SPREF, Context.MODE_PRIVATE);
        usuarioLog = Util.getUsuario(pref);

        list = Util.getDias();
        spDias.setAdapter(new DiasSpinnerAdapter(getActivity(), list));

        spDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dias dia = (Dias) spDias.getSelectedItem();
                buscarReservas(dia.getValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void buscarReservas(int dias) {
        String username = usuarioLog;
        GetReservasByUsername(username, dias);

        recyclerView.setHasFixedSize(true);

        int orientation = MisReservasFragment.this.getResources().getConfiguration().orientation;

        if (orientation == Constants.VERTICAL) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void mostrarDialogDetalle(List<DetalleReservas> list) {
        customDialog = new Dialog(getActivity());
        customDialog.setContentView(R.layout.dialog_detalle_reserva);//establecemos el contenido de nuestro dialog_problema
        TextView txtclose;
        txtclose = (TextView) customDialog.findViewById(R.id.txtclose);

        RecyclerView rvDetalle = customDialog.findViewById(R.id.rvDetalleReserva);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.Adapter adapter = new DetalleReservasAdapter(list, R.layout.rv_detalle_reserva, new DetalleReservasAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(DetalleReservas detalleReserva, int position) {

            }
        });

        rvDetalle.setAdapter(adapter);
        rvDetalle.setLayoutManager(layoutManager);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();
    }

    private void GetReservasByUsername(String username, int dias) {
        String url = Constants.ALQUILER_CANCHAS + "ListadoReservasPorJugador/" + username + "/" + dias;

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    // forma 1
                    Type listType = new TypeToken<ArrayList<ReservasApi>>() {
                    }.getType();
                    final List<ReservasApi> reservas = new Gson().fromJson(response, listType);

                    if (reservas.size() == 0) {
                        Toast.makeText(getContext(), "No hay registros para mostrar", Toast.LENGTH_LONG).show();
                    }
                    adapter = new MisReservasAdapter(reservas, getContext(), new MisReservasAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {
                            TextView txtoptions = view.findViewById(R.id.txtOptions);

                            PopupMenu popup = new PopupMenu(getContext(), txtoptions);
                            //inflating menu from xml resource
                            popup.inflate(R.menu.options_reservas);
                            //adding click listener
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.VerDetalle:
                                            GetDetalleReserva(reservas.get(position).getNumero());
                                            break;
                                    }
                                    return false;
                                }
                            });
                            popup.show();
                        }
                    });
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(getContext(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
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

    private void GetDetalleReserva(int idReserva) {
        String url = Constants.ALQUILER_CANCHAS + "DetalleReserva/" + idReserva;

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    // forma 1
                    Type listType = new TypeToken<ArrayList<DetalleReservas>>() {
                    }.getType();
                    List<DetalleReservas> detalle = new Gson().fromJson(response, listType);

                    if (detalle.size() > 0) {
                        mostrarDialogDetalle(detalle);
                    }
                } else {
                    Toast.makeText(getContext(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
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
}