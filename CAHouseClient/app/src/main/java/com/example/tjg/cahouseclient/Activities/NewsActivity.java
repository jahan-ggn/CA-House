package com.example.tjg.cahouseclient.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseclient.Adapters.NewsAdapter;
import com.example.tjg.cahouseclient.JavaFiles.News;
import com.example.tjg.cahouseclient.Network.VolleyApplication;
import com.example.tjg.cahouseclient.R;
import com.example.tjg.cahouseclient.Storage.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    RecyclerView rvNews;
    AppConfig appConfig;
    List<News> newsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvNews = findViewById(R.id.rvNews);
        rvNews.setHasFixedSize(false);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
        newsList = new ArrayList<>();
        appConfig = new AppConfig();
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayNews();
    }

    public void DisplayNews() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Fetching News");
        progressDialog.show();

        String url = appConfig.getUrl() + "newswebservice/select_client_news.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                newsList.clear();
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("news_array");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String news_id = jsonObject.getString("news_id");
                        String news_title = jsonObject.getString("news_title");
                        String news_description = jsonObject.getString("news_description");
                        String news_date = jsonObject.getString("news_date");
                        String news_time = jsonObject.getString("news_time");

                        News newsActivity = new News(news_id, news_title, news_description, news_date, news_time);
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

