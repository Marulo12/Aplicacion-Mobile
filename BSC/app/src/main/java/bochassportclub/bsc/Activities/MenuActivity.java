package bochassportclub.bsc.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import bochassportclub.bsc.Entities.API_Entities.Code;
import bochassportclub.bsc.Entities.API_Entities.PersonaApi;
import bochassportclub.bsc.Entities.API_Entities.VolleySingleton;
import bochassportclub.bsc.Entities.Constants;
import bochassportclub.bsc.Fragments.MiPerfilFragment;
import bochassportclub.bsc.Fragments.MisClasesFragment;
import bochassportclub.bsc.Fragments.MisReservasFragment;
import bochassportclub.bsc.Fragments.NvaReservaFragment;
import bochassportclub.bsc.Model.MenuModel;
import bochassportclub.bsc.Presenter.MenuPresenter;
import bochassportclub.bsc.R;
import bochassportclub.bsc.Util.Util;
import bochassportclub.bsc.View.MenuView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity implements MenuView {

    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navview)
    NavigationView navview;
    int idJugador = 0;
    private TextView txtUsuarioDL;
    private CircleImageView imgUsuarioDL;
    Dialog mDialog;
    StringRequest stringRequest;
    MenuPresenter presenter;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mDialog = new Dialog(this);
        ButterKnife.bind(this);
        pref = getSharedPreferences(Constants.SPREF, MODE_PRIVATE);
        presenter = new MenuModel(this);
        setToolbar();

        View navHeader = navview.getHeaderView(0);
        txtUsuarioDL = navHeader.findViewById(R.id.txtUsuarioDL);
        imgUsuarioDL = navHeader.findViewById(R.id.imgUsuarioDL);

        idJugador = Util.getIdJugador(pref);
//        if (getIntent().getExtras() != null)
//            idJugador = getIntent().getExtras().getInt("idJugador");

        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logOut:
                        logOut(idJugador);
                        break;
                    case R.id.mis_reservas:
                        changeFragment(new MisReservasFragment(), "Mis Reservas");
                        break;
                    case R.id.mis_clases:
                        changeFragment(new MisClasesFragment(), "Mis Clases Particulares");
                        break;
                    case R.id.mi_perfil:
                        changeFragment(new MiPerfilFragment(), "Mi Perfil");
                        break;
                    case R.id.nva_reserva:
                        changeFragment(new NvaReservaFragment(), "Nueva Reserva");
                        break;
                    case R.id.change_pwd:
                        ShowPopupNvaContrasenia();
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });

        GetPersonaById(idJugador);
        // tomar el idJugador y buscar sus datos en API, llenar imagen y nombre del drawerLayout, cargar en clase jugador y guardar en SharedPreferences
    }

    public void ShowPopupNvaContrasenia() {
        TextView txtclose;
        final EditText etNvaContrasenia;
        Button btnGuardar;
        mDialog.setContentView(R.layout.custom_popup_contrasenia);
        txtclose = mDialog.findViewById(R.id.txtclose);
        etNvaContrasenia = mDialog.findViewById(R.id.etNvaContrasenia);
        btnGuardar = mDialog.findViewById(R.id.btnCambiarPwd);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = etNvaContrasenia.getText().toString();

                if (TextUtils.isEmpty(pwd))
                    etNvaContrasenia.setError("Ingrese Contraseña");
                else if (pwd.length() < 8)
                    etNvaContrasenia.setError("La contraseña debe tener un mínimo de 8 caracteres");
                else {
                    char clave;
                    byte contNumero = 0, contLetraMin = 0;
                    for (byte i = 0; i < pwd.length(); i++) {
                        clave = pwd.charAt(i);
                        String passValue = String.valueOf(clave);

                        if (passValue.matches("[a-z]")) {
                            contLetraMin++;
                        } else if (passValue.matches("[0-9]")) {
                            contNumero++;
                        }
                    }

                    if (contLetraMin == 0)
                        etNvaContrasenia.setError("La contraseña debe tener como mínimo una letra");
                    else if (contNumero == 0)
                        etNvaContrasenia.setError("La contraseña debe tener como mínimo un número");
                    else
                        changePwd(idJugador, pwd);
                }
            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }


    @Override
    public void setToolbar() {
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_hamburguer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void logOut(int idJugador) {
        cerrarSesion(Util.getUsuario(pref));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeFragment(Fragment fragment, String titulo) {
        getSupportFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                // quitar addToBackStack(null) para cerrar app si se hace atras
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle(titulo);
    }

    private void changePwd(final int idJugador, final String pwd) {
        String url = Constants.API_JUGADOR + "ModificarPass";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("IdJugador", idJugador);
            jsonParams.put("contranueva", pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url,
                jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("statusCode").equals("200")) {
                                Toast.makeText(getApplicationContext(), "Su contraseña fue modificada exitosamente", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("pwd", pwd);
                                editor.apply();
                                mDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Ha ocurrido un error, intente luego", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        }) {
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
    }

    private void GetPersonaById(int idJugador) {
        String url = Constants.API_JUGADOR + "GetJugador/" + idJugador;
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    PersonaApi personaApi = gson.fromJson(response, PersonaApi.class);

                    if (personaApi != null) {
                        txtUsuarioDL.setText(personaApi.getApellido() + ' ' + personaApi.getNombre());
                        String url = "http://" + personaApi.getImagen();
                        Picasso.get().load(url).error(R.mipmap.usuario).into(imgUsuarioDL);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void cerrarSesion(String usuario) {
        String url = Constants.API_JUGADOR + "logout/" + usuario;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Code code = gson.fromJson(response, Code.class);
                if (code.getStatusCode().equals("200")) {
                    startActivity(new Intent(getBaseContext(), MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Ha ocurrido un error, Intente luego", Toast.LENGTH_LONG).show();
                }
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