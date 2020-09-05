package com.example.tjg.cahouseclient.Network;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by TJG on 15/10/17.
 */

public class VolleyApplication extends Application {

    public static VolleyApplication volleyApplication;
    public RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        volleyApplication = this;
        requestQueue = Volley.newRequestQueue(this);
    }

    public static final synchronized VolleyApplication getVolleyApplication() {
        return volleyApplication;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}

