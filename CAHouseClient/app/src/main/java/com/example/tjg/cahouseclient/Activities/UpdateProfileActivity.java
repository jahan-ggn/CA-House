package com.example.tjg.cahouseclient.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseclient.Network.VolleyApplication;
import com.example.tjg.cahouseclient.R;
import com.example.tjg.cahouseclient.Storage.AppConfig;
import com.example.tjg.cahouseclient.Storage.MyPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edtFname, edtLname, edtEmail, edtMobile;
    Button btnUpdate;
    MyPreference myPreference;
    AppConfig appConfig;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtFname = findViewById(R.id.edtFname);
        edtLname = findViewById(R.id.edtLname);
        edtEmail = findViewById(R.id.edtEmail);
        edtMobile = findViewById(R.id.edtMobile);
        btnUpdate = findViewById(R.id.btnUpdate);
        myPreference = new MyPreference(this);
        appConfig = new AppConfig();

        btnUpdate.setOnClickListener(this);

        FetchClientProfile();
    }

    @Override
    public void onClick(View v) {
        int flag = 0;

        if (edtFname.getText().toString().length() == 0) {
            edtFname.setError("Required");
            flag = 1;
        }
        if (edtLname.getText().toString().length() == 0) {
            edtLname.setError("Required");
            flag = 1;
        }
        if (edtEmail.getText().toString().length() == 0) {
            edtEmail.setError("Required");
            flag = 1;
        }
        if (edtMobile.getText().toString().length() == 0) {
            edtMobile.setError("Required");
            flag = 1;
        }

        if (flag == 1) return;

        UpdateClientProfile();
    }

    public void FetchClientProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Fetching Profile");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String url = appConfig.getUrl() + "loginwebservice/client_select_profile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);

                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("client_array");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        id = jsonObject.getString("id");
                        edtFname.setText(jsonObject.getString("fname"));
                        edtLname.setText(jsonObject.getString("lname"));
                        edtMobile.setText(jsonObject.getString("mobile"));
                        edtEmail.setText(jsonObject.getString("email"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", myPreference.GetSession());
                return map;
            }
        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }

    public void UpdateClientProfile() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Updating Profile");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String url = appConfig.getUrl() + "loginwebservice/client_update_profile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 1) {
                        Toast.makeText(UpdateProfileActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        myPreference.Logout();
                        startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("fname", edtFname.getText().toString());
                map.put("lname", edtLname.getText().toString());
                map.put("email", edtEmail.getText().toString());
                map.put("mobile", edtMobile.getText().toString());
                return map;
            }
        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
