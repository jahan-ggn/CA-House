package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GetOTPActivity extends AppCompatActivity implements View.OnClickListener {

    public String codesent;
    EditText edtPhoneNumber;
    TextView tvGetOTP, tvBack;
    FirebaseAuth mAuth;
    AppConfig appConfig;
    MyPreference myPreference;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_otp);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        tvBack = findViewById(R.id.tvBack);
        tvGetOTP = findViewById(R.id.tvGetOTP);
        myPreference = new MyPreference(this);
        mAuth = FirebaseAuth.getInstance();
        appConfig = new AppConfig();
        tvBack.setOnClickListener(this);
        tvGetOTP.setOnClickListener(this);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d("complete", phoneAuthCredential.toString());
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("FirebaseException", e.toString());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codesent = s;
                Log.e("codesent", codesent);

                finish();
                Intent intent = new Intent(GetOTPActivity.this, VerifyOTPActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("otpsent", codesent);
                bundle.putString("mobile", edtPhoneNumber.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v == tvBack) {
            finish();
        }

        if (v == tvGetOTP) {
            if (edtPhoneNumber.getText().toString().length() == 0) {
                edtPhoneNumber.setError("Required");
                return;
            }
            checkmobile();
        }
    }

    public void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + edtPhoneNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
        Log.e("phone number", edtPhoneNumber.getText().toString());
    }


    public void checkmobile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Verifying Phone Number");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = appConfig.getUrl() + "loginwebservice/select_admin_mobile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", response);
                    Log.e("status", String.valueOf(jsonObject.getInt("status")));
                    if (jsonObject.getInt("status") == 1) {
                        Toast.makeText(GetOTPActivity.this, "mobile number is correct", Toast.LENGTH_SHORT).show();
                        sendVerificationCode();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(GetOTPActivity.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                        edtPhoneNumber.setError("Invalid Mobile Number");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Log.d("catch", e.toString());
                    Toast.makeText(GetOTPActivity.this, "error in catch block", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("error", error.toString());
                Toast.makeText(GetOTPActivity.this, "error in error listener", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("mobile", edtPhoneNumber.getText().toString());
                Log.e("phone number", edtPhoneNumber.getText().toString());
                return map;
            }
        };

        RetryPolicy retryPolicy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}