package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseadmin.Adapters.FetchClientsAdapter;
import com.example.tjg.cahouseadmin.Adapters.NewsAdapter;
import com.example.tjg.cahouseadmin.JavaFiles.FetchClients;
import com.example.tjg.cahouseadmin.Network.VolleyApplication;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FetchClientsActivity extends AppCompatActivity {

    RecyclerView rvFetchClients;
    List<FetchClients> clientsList;
    AppConfig appConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_clients);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        clientsList = new ArrayList<FetchClients>();
        appConfig = new AppConfig();

        rvFetchClients = findViewById(R.id.rvFetchClients);
        rvFetchClients.setHasFixedSize(false);
        rvFetchClients.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayNews();
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
            Intent intent = new Intent(FetchClientsActivity.this, CreateUserActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void DisplayNews() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Fetching Clients");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = appConfig.getUrl() + "loginwebservice/select_client.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                clientsList.clear();
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray newsArray = jsonObject.getJSONArray("client_array");

                    for (int i = 0; i < newsArray.length(); i++) {

                        jsonObject = newsArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("fname") + " "+jsonObject.getString("lname");
                        String email = jsonObject.getString("email");
                        String mobile = jsonObject.getString("mobile");

                        FetchClients fetchClients = new FetchClients(id, name, email, mobile);
                        clientsList.add(fetchClients);

                    }

                    FetchClientsAdapter fetchClientsAdapter = new FetchClientsAdapter(FetchClientsActivity.this, clientsList);
                    rvFetchClients.setAdapter(fetchClientsAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}