package com.example.tjg.cahouseclient.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseclient.Adapters.DownloadAdapter;
import com.example.tjg.cahouseclient.Network.VolleyApplication;
import com.example.tjg.cahouseclient.R;
import com.example.tjg.cahouseclient.Storage.AppConfig;
import com.example.tjg.cahouseclient.Storage.MyPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FetchDataActivity extends AppCompatActivity {

    ListView listView;
    DownloadAdapter downloadAdapter;
    ArrayList<String> files_on_server = new ArrayList<>();
    ProgressDialog progressDialog;
    AppConfig appConfig;
    MyPreference myPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appConfig = new AppConfig();
        myPreference = new MyPreference(this);
        permission_check();

    }

    public void fetchdata() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Fetching Documents");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String url = appConfig.getUrl() + "upload/fileuploads/download_client.php";

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("upload_array");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        files_on_server.add(jsonObject.getString("filename"));
                    }
                    initialize();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Log.e("catch", e.toString());
                    Toast.makeText(FetchDataActivity.this, "Error in catch block", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("errorwa", error.toString());
                progressDialog.dismiss();
                Toast.makeText(FetchDataActivity.this, "Error in Error Listener " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("cid", myPreference.GetSession());
                return map;
            }
        };

        RetryPolicy retryPolicy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);


    }

    private void permission_check() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                return;
            }
        }

        fetchdata();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchdata();
        } else {
            permission_check();
        }
    }

    private void initialize() {
        listView = findViewById(R.id.listView);
        downloadAdapter = new DownloadAdapter(FetchDataActivity.this, files_on_server);
        listView.setAdapter(downloadAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Downloading...");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
