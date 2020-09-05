package com.example.tjg.cahouseclient.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseclient.Adapters.RequestAdapter;
import com.example.tjg.cahouseclient.Network.VolleyApplication;
import com.example.tjg.cahouseclient.R;
import com.example.tjg.cahouseclient.Storage.AppConfig;
import com.example.tjg.cahouseclient.Storage.MyPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestActivity extends AppCompatActivity implements View.OnClickListener {

    ListView lvAdminRequestedDocuments;
    AppConfig appConfig;
    List<HashMap<String, String>> PendingList = new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> FulfilledList = new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> TotalList = new ArrayList<HashMap<String, String>>();
    MyPreference myPreference;
    RadioGroup rg;
    RadioButton rbPending, rbFulfilled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvAdminRequestedDocuments = findViewById(R.id.lvAdminRequestedDocuments);
        rg = findViewById(R.id.rg);
        rbPending = findViewById(R.id.rbPending);
        rbFulfilled = findViewById(R.id.rbFulfilled);
        appConfig = new AppConfig();
        myPreference = new MyPreference(this);
        rbPending.setOnClickListener(this);
        rbFulfilled.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayRequestDocuments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnAdd) {
            Intent intent = new Intent(RequestActivity.this, AddRequestActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void DisplayRequestDocuments() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Requested Documents");
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.getUrl() + "documentwebservice/select_admin_inserted_documents.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response", response);
                    PendingList.clear();
                    progressDialog.dismiss();
                    int i;
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("request_array");
                    for (i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put("cid", jsonObject1.getString("cid"));
                        Log.e("cid", jsonObject1.getString("cid"));

                        map.put("request_id", jsonObject1.getString("request_id"));
                        Log.e("request_id", jsonObject1.getString("request_id"));

                        map.put("document_id", jsonObject1.getString("document_id"));
                        Log.e("document_id", jsonObject1.getString("document_id"));

                        map.put("requestby", jsonObject1.getString("requestby"));
                        Log.e("requestby", jsonObject1.getString("requestby"));


                        map.put("document_name", jsonObject1.getString("document_name"));
                        Log.e("document_name", jsonObject1.getString("document_name"));

                        map.put("status", jsonObject1.getString("status"));
                        Log.e("status", jsonObject1.getString("status"));

                        TotalList.add(map);
                        lvAdminRequestedDocuments.setAdapter(new RequestAdapter(getApplicationContext(), TotalList,1, RequestActivity.this));

                        if(jsonObject1.getString("status").equals("0")){
                            PendingList.add(map);
                        }

                        if(jsonObject1.getString("status").equals("1")) {
                            FulfilledList.add(map);
                        }
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.e("ERROR", e.getMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("error", error.toString());
                Toast.makeText(RequestActivity.this, "Errror" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);

    }

    @Override
    public void onClick(View v) {
        if(v == rbPending){
            lvAdminRequestedDocuments.setAdapter(new RequestAdapter(getApplicationContext(), PendingList,0, RequestActivity.this));
        }

        if(v == rbFulfilled){
            lvAdminRequestedDocuments.setAdapter(new RequestAdapter(getApplicationContext(), FulfilledList,1, RequestActivity.this));
        }
    }

    public void addToFulFilled(HashMap<String,String> map)
    {
        map.put("status","1");
        FulfilledList.add(map);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
