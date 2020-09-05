package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseadmin.Adapters.FeedbackAdapter;
import com.example.tjg.cahouseadmin.Adapters.NewsAdapter;
import com.example.tjg.cahouseadmin.JavaFiles.Feedback;
import com.example.tjg.cahouseadmin.JavaFiles.News;
import com.example.tjg.cahouseadmin.Network.VolleyApplication;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {

    RecyclerView rvFeedback;
    List<Feedback> feedList;
    AppConfig appConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        feedList = new ArrayList<>();
        appConfig = new AppConfig();

        rvFeedback = findViewById(R.id.rvFeedback);
        rvFeedback.setHasFixedSize(false);
        rvFeedback.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayFeedback();
    }

    public void DisplayFeedback(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Fetching Feedbacks");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = appConfig.getUrl() + "feedbackwebservice/select_feedback.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                feedList.clear();
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("feedback_array");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject = jsonArray.getJSONObject(i);

                        String feedid = jsonObject.getString("feedback_id");
                        String feedname = jsonObject.getString("name");
                        String feedemail = jsonObject.getString("email");
                        String feedmsg = jsonObject.getString("feedbackmsg");
                        String feeddate = jsonObject.getString("feedbackdate");
                        String feedtime = jsonObject.getString("feedbacktime");

                        Feedback feedback = new Feedback(feedid, feedname, feedemail, feedmsg, feeddate, feedtime);
                        feedList.add(feedback);


                    }

                    FeedbackAdapter feedbackAdapter = new FeedbackAdapter(FeedbackActivity.this, feedList);
                    rvFeedback.setAdapter(feedbackAdapter);


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
