package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOTPActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtOTP;
    TextView tvChangePassword, tvBack;
    FirebaseAuth mAuth;
    AppConfig appConfig;
    String codesent;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtOTP = findViewById(R.id.edtOTP);
        tvBack = findViewById(R.id.tvBack);
        tvChangePassword = findViewById(R.id.tvChangePassword);
        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            codesent = bundle.getString("otpsent");
            mobile = bundle.getString("mobile");
        }

        tvBack.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == tvBack) {
            finish();
        }

        if (v == tvChangePassword) {
            if (edtOTP.getText().toString().length() == 0) {
                edtOTP.setError("Required");
                return;
            }

            if (edtOTP.getText().toString().length() < 6) {
                Toast.makeText(this, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyCode();
        }
    }

    public void verifyCode() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, edtOTP.getText().toString());
        Log.d("credential", credential.toString());
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Verifying OTP");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            finish();
                            Intent intent = new Intent(VerifyOTPActivity.this, ForgotPasswordActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("mobile", mobile);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                progressDialog.dismiss();
                                Toast.makeText(VerifyOTPActivity.this, "You Have Entered Wrong OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}

