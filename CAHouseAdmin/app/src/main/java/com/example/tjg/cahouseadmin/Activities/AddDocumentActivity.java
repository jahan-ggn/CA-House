package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
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
import com.example.tjg.cahouseadmin.Network.VolleyApplication;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddDocumentActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtAddDocument;
    Button btnSubmit;
    AppConfig appConfig;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtAddDocument = findViewById(R.id.edtAddDocument);
        btnSubmit = findViewById(R.id.btnSubmit);
        appConfig = new AppConfig();

        btnSubmit.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("document_id");
            edtAddDocument.setText(bundle.getString("document_name"));
            btnSubmit.setText("Edit");
        }
    }

    @Override
    public void onClick(View v) {
        int flag = 0;
        if (edtAddDocument.getText().toString().length() == 0) {
            edtAddDocument.setError("Required");
            flag = 1;
        }
        if (flag == 1) return;

        if (id == null)
            AddDocument();
        else
            UpdateDocument();

    }

    private void UpdateDocument() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Editing Document");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = appConfig.getUrl() + "documentwebservice/update_document.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");

                    if (status == 1) {
                        Toast.makeText(AddDocumentActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(AddDocumentActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                map.put("document_id", id);
                map.put("document_name", edtAddDocument.getText().toString());
                return map;
            }
        };

        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }

    public void AddDocument() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Inserting Document");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = appConfig.getUrl() + "documentwebservice/insert_document.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        Toast.makeText(AddDocumentActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(AddDocumentActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                map.put("document_name", edtAddDocument.getText().toString());
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
