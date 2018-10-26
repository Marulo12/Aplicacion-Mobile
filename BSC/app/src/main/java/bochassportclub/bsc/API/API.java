package bochassportclub.bsc.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    private static final String BASE_URL = "http://marulo12-001-site1.dtempurl.com/api/";

    public static int ALQUILER_CANCHAS = 1;
    public static int AGENDA = 2;
    public static int PERSONAS = 3;

    private static Retrofit retrofit = null;

    public static Retrofit getAPI(int codigo) {
        if (retrofit == null) {
            String base = BASE_URL;
            switch (codigo) {
                case 1:
                    base += "AlquilerCanchas/";
                    break;
                case 2:
                    base += "Agenda/";
                    break;
                case 3:
                    base += "Personas/";
                    break;
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(base)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
