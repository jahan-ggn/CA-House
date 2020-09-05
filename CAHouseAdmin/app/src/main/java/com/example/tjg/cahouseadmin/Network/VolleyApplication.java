package com.example.tjg.cahouseadmin.Network;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


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
