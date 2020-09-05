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
import com.example.tjg.cahouseadmin.Adapters.NewsAdapter;
import com.example.tjg.cahouseadmin.JavaFiles.News;
import com.example.tjg.cahouseadmin.Network.VolleyApplication;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    RecyclerView rvNews;
    List<News> newsList;
    AppConfig appConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newsList = new ArrayList<>();
        appConfig = new AppConfig();

        rvNews = findViewById(R.id.rvNews);
        rvNews.setHasFixedSize(false);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
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
            Intent intent = new Intent(NewsActivity.this, AddNewsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void DisplayNews() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Fetching News");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = appConfig.getUrl() + "newswebservice/select_admin_news.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                newsList.clear();
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray newsArray = jsonObject.getJSONArray("news_array");

                    for (int i = 0; i < newsArray.length(); i++) {

                        jsonObject = newsArray.getJSONObject(i);

                        String news_id = jsonObject.getString("news_id");
                        String news_title = jsonObject.getString("news_title");
                        String news_description = jsonObject.getString("news_description");
                        String news_date = jsonObject.getString("news_date");
                        String news_time = jsonObject.getString("news_time");
                        String news_status = jsonObject.getString("news_status");

                        News newsActivity = new News(news_id, news_title, news_description, news_date, news_time, news_status);
                        newsList.add(newsActivity);

                    }

                    NewsAdapter newsAdapter = new NewsAdapter(NewsActivity.this, newsList);
                    rvNews.setAdapter(newsAdapter);


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