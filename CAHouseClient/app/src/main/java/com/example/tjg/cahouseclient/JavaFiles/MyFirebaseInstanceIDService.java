package com.example.tjg.cahouseclient.JavaFiles;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseclient.Network.VolleyApplication;
import com.example.tjg.cahouseclient.Storage.AppConfig;
import com.example.tjg.cahouseclient.Storage.MyPreference;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    AppConfig appConfig;
    MyPreference myPreference;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        appConfig = new AppConfig();
        myPreference = new MyPreference(this);
        updateGcmToken();
    }

    public void updateGcmToken(){
        String url = appConfig.getUrl() + "updateclientgcm.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if(status == 1){
                        Toast.makeText(MyFirebaseInstanceIDService.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MyFirebaseInstanceIDService.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("mobile", myPreference.getKeyUserMob());
                map.put("gcm", myPreference.getGcmTokenId());
                return map;
            }
        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }
}
