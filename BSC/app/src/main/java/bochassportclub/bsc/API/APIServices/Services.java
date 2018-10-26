package bochassportclub.bsc.API.APIServices;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Services {

    @GET("GetJugador")
    Call<JsonElement> GetJugador (@Query("id") int IdPersona);
}
