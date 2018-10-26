package bochassportclub.bsc.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.io.File;
import java.io.IOException;

import bochassportclub.bsc.Adapters.BarriosAdapter;
import bochassportclub.bsc.Adapters.LocalidadesAdapter;
import bochassportclub.bsc.Adapters.TipoDocumentoAdapter;
import bochassportclub.bsc.Entities.API_Entities.PersonaApi;
import bochassportclub.bsc.Entities.API_Entities.VolleySingleton;
import bochassportclub.bsc.Entities.Constants;
import bochassportclub.bsc.Entities.Localidad;
import bochassportclub.bsc.Entities.Persona;
import bochassportclub.bsc.Entities.TipoDocumento;
import bochassportclub.bsc.Model.MiPerfilModel;
import bochassportclub.bsc.Presenter.MiPerfilPresenter;
import bochassportclub.bsc.R;
import bochassportclub.bsc.Util.Util;
import bochassportclub.bsc.View.MiPerfilView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MiPerfilFragment extends Fragment implements MiPerfilView {

    //region Declaracion
    MiPerfilPresenter presenter;
    @BindView(R.id.imgPerfil)
    CircleImageView img;
    @BindView(R.id.etUsernamePerfil)
    TextView etUsername;
    @BindView(R.id.etNombrePerfil)
    EditText etNombre;
    @BindView(R.id.etApellidoPerfil)
    EditText etApellido;
    @BindView(R.id.spinnerTipoDocPerfil)
    Spinner spTipoDoc;
    @BindView(R.id.etNroDocPerfil)
    EditText etNroDoc;
    @BindView(R.id.etMailPerfil)
    EditText etMail;
    @BindView(R.id.etTelPerfil)
    EditText etTel;
    @BindView(R.id.spinnerLocalidadPerfil)
    Spinner spLocalidad;
    @BindView(R.id.spinnerBarrioPerfil)
    Spinner spBarrio;
    @BindView(R.id.etCallePerfil)
    EditText etCalle;
    @BindView(R.id.etNroCallePerfil)
    EditText etNroCalle;
    @BindView(R.id.btnChangeImg)
    ImageButton btnChangeImg;
    private Unbinder unbinder;
    StringRequest stringRequest;
    SharedPreferences pref;
    String usuarioLog;
    int idJugador = 0;

    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    private static final String CARPETA_PRINCIPAL = "DCIM/";//directorio principal
    private static final String CARPETA_IMAGEN = "BSC_Padel";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path; //almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap;
    //endregion

    public MiPerfilFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mi Perfil");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mi_perfil, container, false);

        unbinder = ButterKnife.bind(this, view);

        presenter = new MiPerfilModel(this);
        pref = getContext().getSharedPreferences(Constants.SPREF, Context.MODE_PRIVATE);
        idJugador = Util.getIdJugador(pref);
        usuarioLog = Util.getUsuario(pref);
        spTipoDoc.setAdapter(new TipoDocumentoAdapter(getContext(), presenter.getTipoDocumentos()));
        spLocalidad.setAdapter(new LocalidadesAdapter(getContext(), presenter.getLocalidades()));
        spLocalidad.setEnabled(false);
//        spTipoDoc.setEnabled(false);
        spBarrio.setEnabled(false);

        GetPersonaById(idJugador);

        //Permisos
        if (solicitaPermisosVersionesSuperiores()) {
            btnChangeImg.setVisibility(View.VISIBLE);
        } else {
            btnChangeImg.setVisibility(View.GONE);
        }
        return view;
    }

    @OnClick(R.id.btnChangeImg)
    public void changeImg() {
        mostrarDialogOpciones();
    }

    @OnClick(R.id.btnModJugador)
    public void modificarJugador() {
        if (UserValidations()) {
            Persona p = new Persona();
            p.setIdUsuario(idJugador);
            p.setNombre(etNombre.getText().toString());
            p.setApellido(etApellido.getText().toString());
            TipoDocumento td = (TipoDocumento) spTipoDoc.getSelectedItem();
            p.setIdTipoDocumento(td.getId());
            p.setMail(etMail.getText().toString());
            p.setTelefono(etTel.getText().toString());
            p.setNroDocumento(etNroDoc.getText().toString());

            editJugador(p);
        }
    }

    public boolean UserValidations() {
        boolean ret = false;

        if (TextUtils.isEmpty(etNombre.getText()))
            etNombre.setError("Ingrese Nombre");
        else if (TextUtils.isEmpty(etApellido.getText()))
            etApellido.setError("Ingrese Apellido");
        else if (TextUtils.isEmpty(etNroDoc.getText()))
            etNroDoc.setError("Ingrese Nro de documento");
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
        else
            ret = true;

        return ret;
    }

    public void mostrarDialogOpciones() {
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    abriCamara();
                } else {
                    if (opciones[i].equals("Elegir de Galeria")) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCIONA);
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    private void abriCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (!isCreada) {
            isCreada = miFile.mkdirs();
        }

        if (isCreada) {
            Long consecutivo = System.currentTimeMillis() / 1000;
            String nombre = consecutivo.toString() + ".jpg";

            path = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN
                    + File.separator + nombre;//indicamos la ruta de almacenamiento

            fileImagen = new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // si version de android es superior a la 7
                String authorities = getContext().getPackageName() + ".provider";
                Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, fileImagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }
            startActivityForResult(intent, COD_FOTO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MIS_PERMISOS:
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {//el dos representa la cantidad de permisos
                    btnChangeImg.setVisibility(View.VISIBLE);
                } else {
                    solicitarPermisosManual();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"Si", "No"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case COD_SELECCIONA:
                    //Obtenemos el 'data' del intent y  lo mostramos en el ImageView
                    Uri miPath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), miPath);
                        img.setImageBitmap(bitmap);
                        bitmap = Util.redimensionarImagen(bitmap, 600, 800);
                        saveImg(Util.convertirImgString(bitmap), usuarioLog + ".jpg", idJugador + "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                }
                            });

                    bitmap = BitmapFactory.decodeFile(path);
                    img.setImageBitmap(bitmap);
                    bitmap = Util.redimensionarImagen(bitmap, 600, 800);
                    saveImg(Util.convertirImgString(bitmap), usuarioLog + ".jpg", idJugador + "");
                    break;
            }
        }
    }

    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)))) {
            cargarDialogoRecomendacion();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MIS_PERMISOS);
        }

        return false;
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MIS_PERMISOS);
            }
        });
        dialogo.show();
    }

    @OnItemSelected(R.id.spinnerLocalidadPerfil)
    public void cargarBarrios() {
        int idl = ((Localidad) spLocalidad.getSelectedItem()).getId();
        spBarrio.setAdapter(new BarriosAdapter(getContext(), presenter.getBarrios(idl)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                        String url = "http://" + personaApi.getImagen();
                        Picasso.get().load(url).error(R.mipmap.usuario).into(img);
                        etUsername.setText(personaApi.getUsuario());
                        etNombre.setText(personaApi.getNombre());
                        etApellido.setText(personaApi.getApellido());
                        spTipoDoc.setId(personaApi.getIdTipoDoc());
                        etNroDoc.setText(personaApi.getDocumento());
                        etMail.setText(personaApi.getMail());
                        etTel.setText(personaApi.getTelefono());
                        spLocalidad.setId(personaApi.getIdLocalidad());
                        spBarrio.setId(personaApi.getIdLocalidad());
                        etCalle.setText(personaApi.getCalle());
                        etNroCalle.setText(personaApi.getNcalle());
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

    private void saveImg(final String img, final String nomImagen, final String idJugador) {
        String url = Constants.API_JUGADOR + "SubirImagen";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("img", img);
            jsonParams.put("nomImagen", nomImagen);
            jsonParams.put("IdJugador", idJugador);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("statusCode").equals("200"))
                                Toast.makeText(getContext(), "Imagen Guardada Correctamente", Toast.LENGTH_SHORT).show();
//                            else {
//                                Toast.makeText(getContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getContext(), "Ha ocurrido un error, intente luego", Toast.LENGTH_SHORT).show();
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

    private void editJugador(Persona p) {
        String url = Constants.API_JUGADOR + "ModificarJugador";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("IdUsuario", p.getIdUsuario());
            jsonParams.put("Nombre", p.getNombre());
            jsonParams.put("Apellido", p.getApellido());
            jsonParams.put("IdTipoDocumento", p.getIdTipoDocumento());
            jsonParams.put("Mail", p.getMail());
            jsonParams.put("Telefono", p.getTelefono());
            jsonParams.put("NroDocumento", p.getNroDocumento());
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
                                Toast.makeText(getContext(), "Su perfil fue modificado exitosamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Ha ocurrido un error, intente luego", Toast.LENGTH_SHORT).show();
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