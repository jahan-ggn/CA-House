package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.tjg.cahouseadmin.Network.VolleyApplication;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;
import com.example.tjg.cahouseadmin.Storage.MyPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    Button btnSubmit;
    AppConfig appConfig = new AppConfig();
    MyPreference myPreference;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);
        myPreference = new MyPreference(this);
    }

    @Override
    public void onClick(View view) {
        int flag = 0;
        if (edtOldPassword.getText().toString().length() == 0) {
            flag = 1;
            edtOldPassword.setError("Required");
        }

        if (edtNewPassword.getText().toString().length() == 0) {
            flag = 1;
            edtNewPassword.setError("Required");
        }

        if (edtConfirmPassword.getText().toString().length() == 0) {
            flag = 1;
            edtConfirmPassword.setError("Required");
        }

        if (!edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            flag = 1;
            Toast.makeText(this, "Password Does Not Match", Toast.LENGTH_SHORT).show();
        }

        if (flag == 1) return;
        ChangeAdminPassword();
    }

    public void ChangeAdminPassword() {
        id = myPreference.getKEY_USER_ID();
        final ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this);
        progressDialog.setMessage("Changing Password");
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = appConfig.getUrl() + "loginwebservice/change_admin_password.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 1) {
                        Toast.makeText(ChangePasswordActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        myPreference.Logout();
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        edtOldPassword.setError("Wrong Password");
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("old_password", edtOldPassword.getText().toString());
                map.put("new_password", edtNewPassword.getText().toString());
                return map;
            }
        };

        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
