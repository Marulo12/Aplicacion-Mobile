package bochassportclub.bsc.Entities.API_Entities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue request;
    private static Context context;

    private VolleySingleton(Context context) {
        VolleySingleton.context = context;
        request = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (request == null) {
            request = Volley.newRequestQueue(context.getApplicationContext());
        }
        return request;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}