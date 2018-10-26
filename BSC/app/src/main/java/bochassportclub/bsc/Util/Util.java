package bochassportclub.bsc.Util;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import bochassportclub.bsc.Entities.Dias;
import bochassportclub.bsc.Entities.Horarios;

public class Util {

    public static boolean isNumeric(String valor) {
        try {
            Integer.parseInt(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<Dias> getDias() {
        ArrayList<Dias> dias = new ArrayList<>();
        dias.add(new Dias(7, "Prox. 7 días"));
        dias.add(new Dias(30, "Prox. 30 días"));
        dias.add(new Dias(-7, "Últimos 7 días"));
        dias.add(new Dias(-30, "Últimos 30 días"));

        return dias;
    }

    public static List<Horarios> getHorarios() {
        ArrayList<Horarios> horarios = new ArrayList<>();
        horarios.add(new Horarios(0, "Seleccione"));
//        horarios.add(new Horarios(1, "17:00"));
//        horarios.add(new Horarios(2, "17:30"));
        horarios.add(new Horarios(3, "18:00"));
        horarios.add(new Horarios(4, "18:30"));
        horarios.add(new Horarios(5, "19:00"));
        horarios.add(new Horarios(6, "19:30"));
        horarios.add(new Horarios(7, "20:00"));
        horarios.add(new Horarios(8, "20:30"));
        horarios.add(new Horarios(9, "21:00"));
        horarios.add(new Horarios(10, "21:30"));
        horarios.add(new Horarios(11, "22:00"));
        horarios.add(new Horarios(12, "22:30"));
        horarios.add(new Horarios(13, "23:00"));
        horarios.add(new Horarios(14, "23:30"));
        horarios.add(new Horarios(15, "23:59"));
        return horarios;
    }

    public static Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);

            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);

        } else {
            return bitmap;
        }
    }

    public static String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        return Base64.encodeToString(imagenByte, Base64.DEFAULT);
    }

    public static String getPwd(SharedPreferences pref) {
        return pref.getString("pwd", "");
    }

    public static String getUsuario(SharedPreferences pref) {
        return pref.getString("username", "");
    }

    public static int getIdJugador(SharedPreferences pref) {
        return pref.getInt("idJugador", 0);
    }
}