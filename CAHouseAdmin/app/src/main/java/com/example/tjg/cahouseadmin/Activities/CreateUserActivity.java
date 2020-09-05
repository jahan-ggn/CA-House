package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUserActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtFirstName, edtLastName, edtEmail, edtUserMobile, edtPassword, edtConfirmPassword;
    Button btnCreate;
    AppConfig appConfig = new AppConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        edtUserMobile = findViewById(R.id.edtUserMobile);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int flag = 0;
        if (edtFirstName.getText().toString().length() == 0) {
            edtFirstName.setError("Required");
            flag = 1;
        }
        if (edtLastName.getText().toString().length() == 0) {
            edtLastName.setError("Required");
            flag = 1;
        }

        if (edtPassword.getText().toString().length() == 0) {
            edtPassword.setError("Required");
            flag = 1;
        }
        if (edtConfirmPassword.getText().toString().length() == 0) {
            edtConfirmPassword.setError("Required");
            flag = 1;
        }

        if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            Toast.makeText(CreateUserActivity.this, "Password Not Match", Toast.LENGTH_LONG).show();
            flag = 1;
        }

        String validmobile = "^[6-9]\\d{9}$";
        String mob = edtUserMobile.getText().toString();
        Matcher mobmatcher = Pattern.compile(validmobile).matcher(mob);

        if (!mobmatcher.matches()) {
            edtUserMobile.setError("Invalid Mobile");
            flag = 1;
        }

        String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                "\\@" +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                "(" +

                "\\." +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                ")+";


        String email = edtEmail.getText().toString();

        Matcher emailmatcher = Pattern.compile(validemail).matcher(email);


        if (!emailmatcher.matches()) {
            edtEmail.setError("Invalid Email");
            flag = 1;
        }

        if (flag == 1) return;

        CreateUser();


    }

    public void CreateUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Saving User");
        progressDialog.setCancelable(false);
        progressDialog.show();


        String url = appConfig.getUrl() + "loginwebservice/insert_user.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(CreateUserActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateUserActivity.this, DashboardActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("fname", edtFirstName.getText().toString());
                map.put("lname", edtLastName.getText().toString());
                map.put("email", edtEmail.getText().toString());
                map.put("mobile", edtUserMobile.getText().toString());
                map.put("password", edtPassword.getText().toString());
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
