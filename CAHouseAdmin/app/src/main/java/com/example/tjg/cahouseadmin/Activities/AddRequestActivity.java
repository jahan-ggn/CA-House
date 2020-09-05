package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddRequestActivity extends AppCompatActivity {

    Spinner spClient;
    ListView lvDocuments;
    Button btnSave, btnCancel;
    AppConfig appConfig;
    ArrayAdapter<String> adapter;
    ArrayList<String> lstDocumentId = new ArrayList<>();
    ArrayList<String> lstDocumentName = new ArrayList<>();
    ArrayList<String> lstClientName = new ArrayList<>();
    ArrayList<String> lstClientId = new ArrayList<>();
    MyPreference myPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spClient = findViewById(R.id.spClient);
        lvDocuments = findViewById(R.id.lvDocuments);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        appConfig = new AppConfig();
        myPreference = new MyPreference(this);

        DisplayClients();


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
                    Log.e("catch", e.getMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                Toast.makeText(AddRequestActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }

    public void DisplayClients() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Fetching Clients");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = appConfig.getUrl() + "documentwebservice/client_list.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("client_array");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        lstClientId.add(jsonObject.getString("id"));
                        lstClientName.add(jsonObject.getString("fname") + " " + jsonObject.getString("lname"));
                    }
                    adapter = new ArrayAdapter<String>(AddRequestActivity.this, android.R.layout.simple_list_item_1, lstClientName);
                    spClient.setAdapter(adapter);
                    DisplayDocuments();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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


        String url = appConfig.getUrl() + "documentwebservice/insert_admin_request.php";
        Log.e("url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);
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
                    Log.e("catchblock", e.toString());
                    Toast.makeText(AddRequestActivity.this, "Error In Catch Block", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AddRequestActivity.this, "Error in Error Listener" + error, Toast.LENGTH_LONG).show();
                Log.e("error", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("document_id", arrDocId);
                Log.e("document_id", arrDocId.toString());
                map.put("ca_id", myPreference.GetSession());
                Log.e("ca_id", myPreference.GetSession());
                map.put("requestby", myPreference.getName());
                Log.e("requestby", myPreference.getName());
                map.put("cid", lstClientId.get(spClient.getSelectedItemPosition()));
                Log.e("cid", lstClientId.get(spClient.getSelectedItemPosition()));
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
