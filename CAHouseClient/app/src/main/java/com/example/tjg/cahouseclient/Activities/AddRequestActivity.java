package com.example.tjg.cahouseclient.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddRequestActivity extends AppCompatActivity {

    ListView lvDocuments;
    Button btnSave, btnCancel;
    AppConfig appConfig;
    ArrayAdapter<String> adapter;
    ArrayList<String> lstDocumentId = new ArrayList<>();
    ArrayList<String> lstDocumentName = new ArrayList<>();

    MyPreference myPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvDocuments = findViewById(R.id.lvDocuments);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        appConfig = new AppConfig();
        myPreference = new MyPreference(this);

        DisplayDocuments();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = "";
                int len = lvDocuments.getCount();
                JSONArray jsonDocId = new JSONArray();
                SparseBooleanArray checked = lvDocuments.getCheckedItemPositions();
                for (int i = 0; i < len; i++)
                    if (checked.get(i)) {
                        String item = lstDocumentName.get(i);
                        temp = temp + item + ", ";
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("doc_id", lstDocumentId.get(i).toString());
                            jsonDocId.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                InsertRequest(jsonDocId.toString());
            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
    }

    public void DisplayDocuments() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Fetching Documents");
        progressDialog.setCancelable(false);
        progressDialog.show();

        lstDocumentId.clear();
        lstDocumentName.clear();

        String url = appConfig.getUrl() + "documentwebservice/request_document.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("requestdocument_array");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        lstDocumentId.add(jsonObject.getString("document_id"));
                        lstDocumentName.add(jsonObject.getString("document_name"));
                    }

                    adapter = new ArrayAdapter<String>(AddRequestActivity.this, android.R.layout.simple_list_item_multiple_choice, lstDocumentName);
                    lvDocuments.setAdapter(adapter);
                    lvDocuments.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddRequestActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }

    public void InsertRequest(final String arrDocId) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Request");
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        String url = appConfig.getUrl() + "documentwebservice/insert_client_request.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        Toast.makeText(AddRequestActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    if (status == 0) {
                        Toast.makeText(AddRequestActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddRequestActivity.this, "Error In Catch Block", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AddRequestActivity.this, "Error in Error Listener" + error, Toast.LENGTH_LONG).show();
                Log.d("###", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("document_id", arrDocId);
                map.put("cid", myPreference.GetSession());
                map.put("requestby", myPreference.getName());
                return map;
            }
        };

        RetryPolicy retryPolicy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
