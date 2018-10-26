package bochassportclub.bsc.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import bochassportclub.bsc.Adapters.NoticiasAdapter;
import bochassportclub.bsc.Datos.bd;
import bochassportclub.bsc.Entities.API_Entities.DetalleReservas;
import bochassportclub.bsc.Entities.API_Entities.VolleySingleton;
import bochassportclub.bsc.Entities.Constants;
import bochassportclub.bsc.Entities.Noticias;
import bochassportclub.bsc.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rvNoticias)
    RecyclerView rvNoticias;
    Dialog customDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getNoticias();
    }

    @OnClick(R.id.btnIngresar)
    public void mostrarLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("opcion", 1);
        startActivity(intent);
    }

    @OnClick(R.id.btnRegistro)
    public void mostrarSignUp() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("opcion", 2);
        startActivity(intent);
    }

    private void getNoticias() {
        String url = Constants.ALQUILER_CANCHAS + "Noticias";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // forma 1
                Type listType = new TypeToken<ArrayList<Noticias>>() {
                }.getType();
                List<Noticias> noticias = new Gson().fromJson(response, listType);

                if (noticias.size() > 0) {
                    RecyclerView.Adapter adapter = new NoticiasAdapter(noticias, new NoticiasAdapter.onClickListener() {
                        @Override
                        public void onItemClickListener(Noticias n, int position) {
                            customDialog = new Dialog(MainActivity.this);
                            customDialog.setContentView(R.layout.dialog_news);

                            TextView txtDsDialog = customDialog.findViewById(R.id.txtDsDialog);
                            ImageView imgDialog = customDialog.findViewById(R.id.imgDialog);
                            Button btnCerrar = customDialog.findViewById(R.id.btnCerrarDialog);

                            btnCerrar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialog.dismiss();
                                }
                            });

                            customDialog.setTitle(n.getTitulo());
                            Picasso.get().load(n.getUrl()).into(imgDialog);
                            txtDsDialog.setText(n.getDescripcion());
                            customDialog.show();
                        }
                    });

                    RecyclerView.LayoutManager manager;

                    manager = new LinearLayoutManager(getApplicationContext());

                    rvNoticias.setLayoutManager(manager);
                    rvNoticias.setAdapter(adapter);
                } else
                    Toast.makeText(getApplicationContext(), "No hay noticias para mostrar", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}