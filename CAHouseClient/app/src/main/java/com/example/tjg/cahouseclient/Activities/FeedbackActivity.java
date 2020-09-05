package com.example.tjg.cahouseclient.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedbackActivity extends AppCompatActivity {

    EditText edtFeedbackName, edtFeedbackEmail, edtFeedbackMsg;
    Button btnFeedback;
    AppConfig appConfig;
    MyPreference myPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appConfig = new AppConfig();
        edtFeedbackName = findViewById(R.id.edtFeedbackName);
        edtFeedbackEmail = findViewById(R.id.edtFeedbackEmail);
        edtFeedbackMsg = findViewById(R.id.edtFeedbackMsg);
        btnFeedback = findViewById(R.id.btnFeedback);
        myPreference = new MyPreference(this);

        edtFeedbackName.setText(myPreference.getName());
        edtFeedbackEmail.setText(myPreference.getEmailId());

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                if (edtFeedbackName.getText().toString().trim().length() == 0) {
                    flag = 1;
                    edtFeedbackName.setError("Required");
                }
                String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                        "\\@" +

                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                        "(" +

                        "\\." +

                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                        ")+";


                String email = edtFeedbackEmail.getText().toString();

                Matcher matcher = Pattern.compile(validemail).matcher(email);


                if (!matcher.matches()) {
                    edtFeedbackEmail.setError("Invalid Email");
                    flag = 1;
                }
                if (edtFeedbackMsg.getText().toString().trim().length() == 0) {
                    flag = 1;
                    edtFeedbackMsg.setError("Required");
                }

                if (flag == 1) return;

                SendFeedback();

            }

            private void SendFeedback() {
                final ProgressDialog progressDialog = new ProgressDialog(FeedbackActivity.this);
                progressDialog.setTitle("Please Wait...");
                progressDialog.setMessage("Sending Feeback");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String url = appConfig.getUrl() + "feedbackwebservice/insert_feedback.php";
                Log.e("URL", url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Log.e("Response", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getInt("status") == 1) {
                                        Toast.makeText(FeedbackActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(FeedbackActivity.this, DashboardActivity.class));
                                    }
                                    else{
                                        Toast.makeText(FeedbackActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Log.e("Response Error", error.toString());
                                Toast.makeText(FeedbackActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                            }

                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", edtFeedbackName.getText().toString());
                        map.put("email", edtFeedbackEmail.getText().toString());
                        map.put("feedbackMsg", edtFeedbackMsg.getText().toString());
                        return map;
                    }
                };

                RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);

                VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}