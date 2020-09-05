package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtMobile, edtPassword;
    Button btnLogin;
    ImageView ivCAHouseLogo;
    TextView tvForgotPassword;
    AppConfig appConfig;
    MyPreference myPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtMobile = findViewById(R.id.edtMobile);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        ivCAHouseLogo = findViewById(R.id.ivCAHouseLogo);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        appConfig = new AppConfig();

        edtMobile.setBackgroundResource(R.drawable.edittext_border);
        edtPassword.setBackgroundResource(R.drawable.edittext_border);
        btnLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        myPreference = new MyPreference(this);
        if (myPreference.GetSession() != null) {
            finish();
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            int flag = 0;
            if (edtMobile.getText().toString().length() == 0) {
                flag = 1;
                edtMobile.setError("Required");
            }
            if (edtPassword.getText().toString().length() == 0) {
                flag = 1;
                edtPassword.setError("Required");
            }
            if (flag == 1) return;
            CheckUser();
        }
        if (view == tvForgotPassword) {
            startActivity(new Intent(MainActivity.this, GetOTPActivity.class));
        }
    }

    public void CheckUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Logging In");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String gcmtoken = FirebaseInstanceId.getInstance().getToken();

        String url = appConfig.getUrl() + "loginwebservice/admin_check_user.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    if (jsonObject.getInt("status") == 1) {
                        myPreference.CreateSession(jsonObject.getString("user_id"));
                        myPreference.setName(jsonObject.getString("fname") + " " + jsonObject.getString("lname"));
                        myPreference.setGcmTokenId(gcmtoken);
                        myPreference.setKeyUserMob(edtMobile.getText().toString());
                        finish();
                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
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
                map.put("mobile", edtMobile.getText().toString());
                map.put("password", edtPassword.getText().toString());
                map.put("gcm", gcmtoken);

                return map;
            }
        };

        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit?");
        builder.setMessage("Do you really want to exit?");
        builder.setIcon(R.drawable.icon);


        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
