package bochassportclub.bsc.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
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

import bochassportclub.bsc.Adapters.DiasSpinnerAdapter;
import bochassportclub.bsc.Adapters.MisClasesAdapter;
import bochassportclub.bsc.Entities.API_Entities.ClaseParticularApi;
import bochassportclub.bsc.Entities.API_Entities.VolleySingleton;
import bochassportclub.bsc.Entities.Constants;
import bochassportclub.bsc.Entities.Dias;
import bochassportclub.bsc.R;
import bochassportclub.bsc.Util.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MisClasesFragment extends Fragment {

    @BindView(R.id.rvMisClases)
    RecyclerView recyclerView;
    @BindView(R.id.spDias)
    Spinner spDias;
    List<Dias> list;
    private Unbinder unbinder;
    StringRequest stringRequest;
    SharedPreferences pref;
    String usuarioLog;

    public MisClasesFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mis Clases Particulares");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_clases, container, false);

        unbinder = ButterKnife.bind(this, view);
        pref = getContext().getSharedPreferences(Constants.SPREF, Context.MODE_PRIVATE);
        usuarioLog = Util.getUsuario(pref);

        list = Util.getDias();

        spDias.setAdapter(new DiasSpinnerAdapter(getActivity(), list));

        spDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dias dia = (Dias) spDias.getSelectedItem();
                buscarClases(dia.getValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void buscarClases(int dias) {
        String username = usuarioLog;
        GetClasesByUsername(username, dias);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void GetClasesByUsername(String username, int dias) {

        String url = Constants.CLASES_PARTICULARES + "/mostrarclases/" + username + "/" + dias;

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    Type listType = new TypeToken<ArrayList<ClaseParticularApi>>() {
                    }.getType();
                    final List<ClaseParticularApi> clases = new Gson().fromJson(response, listType);
                    RecyclerView.Adapter adapter = new MisClasesAdapter(clases, getContext(), new MisClasesAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {
                        }
                    });

                    recyclerView.setAdapter(adapter);

                    if (clases.size() == 0) {
                        Toast.makeText(getContext(), "No registras clases particulares para la fecha seleccionada", Toast.LENGTH_LONG).show();
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