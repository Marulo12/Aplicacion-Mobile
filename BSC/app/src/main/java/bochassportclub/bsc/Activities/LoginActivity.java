package bochassportclub.bsc.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import bochassportclub.bsc.Adapters.BarriosAdapter;
import bochassportclub.bsc.Adapters.LocalidadesAdapter;
import bochassportclub.bsc.Adapters.TipoDocumentoAdapter;
import bochassportclub.bsc.Datos.bd;
import bochassportclub.bsc.Entities.API_Entities.Code;
import bochassportclub.bsc.Entities.API_Entities.VolleySingleton;
import bochassportclub.bsc.Entities.Barrio;
import bochassportclub.bsc.Entities.Constants;
import bochassportclub.bsc.Entities.Domicilio;
import bochassportclub.bsc.Entities.Jugador;
import bochassportclub.bsc.Entities.Localidad;
import bochassportclub.bsc.Entities.Persona;
import bochassportclub.bsc.Entities.TipoDocumento;
import bochassportclub.bsc.Entities.Usuario;
import bochassportclub.bsc.Model.LoginModel;
import bochassportclub.bsc.Presenter.LoginPresenter;
import bochassportclub.bsc.R;
import bochassportclub.bsc.Util.Util;
import bochassportclub.bsc.View.LoginView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class LoginActivity extends AppCompatActivity implements LoginView {

    //region BindUI
    private int on = View.VISIBLE;
    private int off = View.GONE;
    LoginPresenter loginPresenter;
    Jugador jug;
    Domicilio dom;
    Persona per;
    Usuario usu;
    SharedPreferences pref;

    //region Vistas
    @BindView(R.id.vGetPass)
    View vPass;
    @BindView(R.id.vLogIn)
    View vLogin;
    @BindView(R.id.vSignUp)
    View vSignUp;
    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;
    //endregion
    //region Login
    @BindView(R.id.etUsuarioL)
    EditText usuario;
    @BindView(R.id.etPassL)
    EditText pass;
    //endregion
    //region SignUp
    @BindView(R.id.spinnerTipoDoc)
    Spinner spinnerTipoDoc;
    @BindView(R.id.etNroDoc)
    EditText etNroDoc;
    @BindView(R.id.etNombre)
    EditText etNombre;
    @BindView(R.id.etApellido)
    EditText etApellido; // OntextChange
    @BindView(R.id.etMail)
    EditText etMail;
    @BindView(R.id.etTel)
    EditText etTel;
    @BindView(R.id.spinnerBarrio)
    Spinner spinnerBarrio;
    @BindView(R.id.spinnerLocalidad)
    Spinner spinnerLocalidad;
    @BindView(R.id.etCalle)
    EditText etCalle;
    @BindView(R.id.etNroCalle)
    EditText etNroCalle;
    @BindView(R.id.etPiso)
    EditText etPiso;
    @BindView(R.id.etDepto)
    EditText etDepto;
    @BindView(R.id.etUsuario)
    EditText etUsuario;
    @BindView(R.id.etPass)
    EditText etPass;
    //endregion
    //region getPass
    @BindView(R.id.etEmailPass)
    TextView etEmailPass;
    //endregion

    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // boton atras del activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        pref = getSharedPreferences(Constants.SPREF, MODE_PRIVATE);
        setToolbar();
        loginPresenter = new LoginModel(this);
        Bundle bundle = getIntent().getExtras();
        int opcion = bundle.getInt("opcion");

        spinnerTipoDoc.setAdapter(new TipoDocumentoAdapter(this, loginPresenter.getTipoDocumentos()));
        spinnerLocalidad.setAdapter(new LocalidadesAdapter(this, loginPresenter.getLocalidades()));
        setCredencialsIfExist();

        if (opcion == 1)
            showLogIn();
        else if (opcion == 2) {
            showSignUp();
            jug = new Jugador();
            per = new Persona();
            dom = new Domicilio();
            usu = new Usuario();
        } else // por defecto
            showLogIn();
    }

    @OnItemSelected(R.id.spinnerLocalidad)
    public void cargarBarrios() {
        int idl = ((Localidad) spinnerLocalidad.getSelectedItem()).getId();
        spinnerBarrio.setAdapter(new BarriosAdapter(this, loginPresenter.getBarrios(idl)));
    }

    @OnClick(R.id.tvRecuperar)
    public void recuperarPass() {
        showGetPass();
    }

    @OnClick(R.id.btnLogin)
    public void iniciarSesion() {
        if (loginValidations())
            iniciarSesion(usuario.getText().toString(), pass.getText().toString());
            //loginPresenter.performLogin(usuario.getText().toString(), pass.getText().toString());
    }

    @OnClick(R.id.btnSignUp)
    public void registrarJugador() {
        if (signUpValidations()) {

            dom.setIdBarrio(((Barrio) spinnerBarrio.getSelectedItem()).getId());
            dom.setIdLocalidad(((Localidad) spinnerLocalidad.getSelectedItem()).getId());
            dom.setNumero(Integer.parseInt(etNroCalle.getText().toString()));
            dom.setCalle(etCalle.getText().toString());
            dom.setDepartamento(etDepto.getText().toString());

            if (TextUtils.isEmpty(etPiso.getText()))
                dom.setPiso(0);
            else
                dom.setPiso(Integer.parseInt(etPiso.getText().toString()));

            usu.setNombre(etUsuario.getText().toString());
            usu.setContrasenia(Util.md5(etPass.getText().toString().trim()));

            per.setNroDocumento(etNroDoc.getText().toString());
            per.setIdTipoDocumento(((TipoDocumento) spinnerTipoDoc.getSelectedItem()).getId());
            per.setNombre(etNombre.getText().toString());
            per.setApellido(etApellido.getText().toString());
            per.setMail(etMail.getText().toString());
            per.setTelefono(etTel.getText().toString());
            per.setTipo("JUGADOR");

            loginPresenter.performSignUp(dom, usu, per);
        }
    }

    @OnClick(R.id.btnGetPass)
    public void recuperarContrasenia() {
        if (getPwdValidations())
            resetearContraseña(etEmailPass.getText().toString());
    }

    @Override
    public void loginSuccess(int idJugador) {
        setSharedPreferences(usuario.getText().toString(), pass.getText().toString(), idJugador);
        loginPresenter.registrarInicioSession(idJugador);
    }

    @Override
    public void loginError() {
        Toast.makeText(this, "Usuario y/o password incorrectos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean loginValidations() {
        boolean ret = false;

        if (TextUtils.isEmpty(usuario.getText()))
            usuario.setError("Ingrese Usuario");
        else if (TextUtils.isEmpty(pass.getText()))
            pass.setError("Ingrese Password");
        else
            ret = true;

        return ret;
    }

    @Override
    public boolean signUpValidations() {
        boolean ret = false;

        if (TextUtils.isEmpty(etNroDoc.getText()))
            etNroDoc.setError("Ingrese Nro de documento");
        else if (!bd.isValidDninumber(etNroDoc.getText().toString()))
            duplicateDniNumber();
        else if (TextUtils.isEmpty(etNombre.getText()))
            etNombre.setError("Ingrese Nombre");
        else if (TextUtils.isEmpty(etApellido.getText()))
            etApellido.setError("Ingrese Apellido");
        else if (TextUtils.isEmpty(etMail.getText()))
            etMail.setError("Ingrese Mail");
        else if (!Util.isValidEmail(etMail.getText().toString()))
            etMail.setError("Mail: Formato Incorrecto");
        else if (TextUtils.isEmpty(etTel.getText()))
            etTel.setError("Ingrese Teléfono");
        else if (TextUtils.isEmpty(etCalle.getText()))
            etCalle.setError("Ingrese Calle");
        else if (TextUtils.isEmpty(etNroCalle.getText()))
            etNroCalle.setError("Ingrese Numeración");
        else if (TextUtils.isEmpty(etUsuario.getText()))
            etUsuario.setError("Ingrese Usuario");
        else if (!bd.isValidUserName(etUsuario.getText().toString()))
            duplicateUserName();
        else if (validatePass())
            ret = true;

        return ret;
    }

    @Override
    public boolean validatePass() {
        boolean ret = false;
        String pwd = etPass.getText().toString();

        if (TextUtils.isEmpty(etPass.getText()))
            etPass.setError("Ingrese Contraseña");
        else if (pwd.length() < 7)
            etPass.setError("La contraseña debe tener un mínimo de 8 caracteres");
        else {
            char clave;
            byte contNumero = 0, contLetraMin = 0;
            for (byte i = 0; i < pwd.length(); i++) {
                clave = pwd.charAt(i);
                String passValue = String.valueOf(clave);
//            if (passValue.matches("[A-Z]")) {
//                contLetraMay++;
//            } else
                if (passValue.matches("[a-z]")) {
                    contLetraMin++;
                } else if (passValue.matches("[0-9]")) {
                    contNumero++;
                }
            }

            if (contLetraMin == 0)
                etPass.setError("La contraseña debe tener como mínimo una letra");
            else if (contNumero == 0)
                etPass.setError("La contraseña debe tener como mínimo un número");
            else
                ret = true;
        }
        return ret;
    }

    @Override
    public void signUpSuccess(int idJugador) {
        setSharedPreferences(etUsuario.getText().toString(), etPass.getText().toString(), idJugador);
        loginPresenter.registrarInicioSession(idJugador);
    }

    @Override
    public void signUpError() {
        Toast.makeText(this, "No se ha podido completar el registro \n Intente luego", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void duplicateUserName() {
        etUsuario.setError("Nombre de usuario no disponible");
    }

    @Override
    public void duplicateDniNumber() {
        etNroDoc.setError("Ya existe un usuario con este dni");
    }

    @Override
    public void showLogIn() {
        showViews(on, off, off);
    }

    @Override
    public void showSignUp() {
        showViews(off, on, off);
    }

    @Override
    public void showGetPass() {
        showViews(off, off, on);
    }

    @Override
    public void redirectMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void setSharedPreferences(String username, String pwd, int idJugador) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", username);
        editor.putString("pwd", pwd);
        editor.putInt("idJugador", idJugador);
        editor.apply();
    }

    @Override
    public void setCredencialsIfExist() {
        String username = Util.getUsuario(pref);
        String pwd = Util.getPwd(pref);

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            usuario.setText(username);
            pass.setText(pwd);
        }
    }

    @Override
    public boolean getPwdValidations() {
        boolean isValid = false;

        if (TextUtils.isEmpty(etEmailPass.getText()))
            etEmailPass.setError("Ingrese email");
        else if (!Util.isValidEmail(etEmailPass.getText().toString()))
            etEmailPass.setError("Formato incorrecto");
        else
            isValid = true;

        return isValid;
    }

    private void showViews(int login, int signup, int getPass) {
        vLogin.setVisibility(login);
        vSignUp.setVisibility(signup);
        vPass.setVisibility(getPass);
    }

    private void setToolbar() {
        setSupportActionBar(myToolbar);
        //        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.logo_jm);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void resetearContraseña(String email) {
        String url = Constants.API_JUGADOR + "ResetearContraseña/" + email;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Code code = gson.fromJson(response, Code.class);
                if (code.getStatusCode().equals("200")) {
                    etEmailPass.setText("");
                    showLogIn();
                    Toast.makeText(getApplicationContext(), "Su contraseña ha sido reseteada. Verifique su mail", Toast.LENGTH_LONG).show();
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

    private void iniciarSesion(final String usuario, final String contraseña) {
        String url = Constants.API_JUGADOR + "Login";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Usuario", usuario);
            jsonParams.put("Contra", contraseña);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int idUsuario = response.getInt("idUsuario");

                            if (idUsuario == 0) {
                                Toast.makeText(getApplicationContext(), "Credenciales erróneas", Toast.LENGTH_LONG).show();
                            } else if (idUsuario == -1) {
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                            } else {
                                setSharedPreferences(usuario, contraseña, idUsuario);
                                redirectMenu();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Ha ocurrido un error, intente luego", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        }) {
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
    }
}