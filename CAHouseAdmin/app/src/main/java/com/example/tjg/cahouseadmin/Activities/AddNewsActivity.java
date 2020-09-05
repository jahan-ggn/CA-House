package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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

public class AddNewsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtNewsTitle, edtNewsDescription;
    Button btnAnnounceNews;
    AppConfig appConfig;
    String id;
    RadioGroup rgStatus;
    RadioButton rbActive, rbDeactive, rbStatus;
    String rbStat;
    ScrollView svNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        svNews = findViewById(R.id.svNews);
        edtNewsTitle = findViewById(R.id.edtNewsTitle);
        edtNewsDescription = findViewById(R.id.edtNewsDescription);
        rgStatus = findViewById(R.id.rgStatus);
        rbActive = findViewById(R.id.rbActive);
        rbDeactive = findViewById(R.id.rbDeactive);
        appConfig = new AppConfig();
        btnAnnounceNews = findViewById(R.id.btnAnnounceNews);
        btnAnnounceNews.setOnClickListener(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("news_id");
            edtNewsTitle.setText(bundle.getString("news_title"));
            edtNewsDescription.setText(bundle.getString("news_description"));
            rbStat = bundle.getString("news_status");
            if (rbStat.equals("Active")) {
                rbActive.setChecked(true);
            } else {
                rbDeactive.setChecked(true);
            }
            btnAnnounceNews.setText("Edit");
        }
    }

    @Override
    public void onClick(View view) {
        rbStatus = findViewById(rgStatus.getCheckedRadioButtonId());
        rbStat = rbStatus.getText().toString();


        int flag = 0;

        if (edtNewsTitle.getText().toString().length() == 0) {
            edtNewsTitle.setError("Required");
            flag = 1;
        }

        if (edtNewsDescription.getText().toString().length() == 0) {
            edtNewsDescription.setError("Required");
            flag = 1;
        }


        if (flag == 1) return;

        if (id == null) {
            InsertNews();
        } else {

            UpdateNews();
        }


    }

    private void InsertNews() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Inserting News");
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        String url = appConfig.getUrl() + "newswebservice/insert_news.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        Toast.makeText(AddNewsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    if (status == 0) {
                        Toast.makeText(AddNewsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("catch", e.getMessage());
                    Toast.makeText(AddNewsActivity.this, "Error In Catch Block", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AddNewsActivity.this, "Error in Error Listener" + error, Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("news_title", edtNewsTitle.getText().toString());
                map.put("news_description", edtNewsDescription.getText().toString());
                map.put("news_status", rbStat);
                Log.e("news_status", rbStat);
                return map;
            }
        };


        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }


    public void UpdateNews() {

        final ProgressDialog progressDialog = new ProgressDialog(AddNewsActivity.this);
        progressDialog.setMessage("Editing News");
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = appConfig.getUrl() + "newswebservice/update_news.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 1) {
                        Toast.makeText(AddNewsActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddNewsActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                map.put("news_id", id);
                map.put("news_title", edtNewsTitle.getText().toString());
                map.put("news_description", edtNewsDescription.getText().toString());
                map.put("news_status", rbStat);
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
