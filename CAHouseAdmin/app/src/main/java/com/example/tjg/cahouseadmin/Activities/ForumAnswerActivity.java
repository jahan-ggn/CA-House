package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseadmin.Adapters.ForumAnswerAdpter;
import com.example.tjg.cahouseadmin.Network.VolleyApplication;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;
import com.example.tjg.cahouseadmin.Storage.MyPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumAnswerActivity extends AppCompatActivity {
    TextView tvClientName, tvDate, tvQuestion, tvTotalAnswers;
    EditText edtAnswer;
    String passid;
    ListView lvAnswerList;
    AppConfig appConfig;
    MyPreference myPreference;
    FloatingActionButton fabSend;

    List<HashMap<String, String>> answerList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_answer);

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appConfig = new AppConfig();
        myPreference = new MyPreference(this);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvClientName = findViewById(R.id.tvAdminName);
        tvDate = findViewById(R.id.tvDate);
        tvTotalAnswers = findViewById(R.id.tvTotalAnswers);
        fabSend = findViewById(R.id.fabsend);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAnswer.getText().toString().equals("")) {
                    edtAnswer.setError("Please Enter Answer First");
                } else {
                    InsertAnswer();
                }
            }
        });

        edtAnswer = findViewById(R.id.edtAnswer);

        lvAnswerList = findViewById(R.id.lvAnswerList);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            passid = bundle.getString("id");
            Log.e("passid", passid);

            tvQuestion.setText(bundle.getString("question"));
            Log.e("question", bundle.getString("question"));
            tvClientName.setText(bundle.getString("questionby"));
            tvDate.setText(bundle.getString("date"));
            FetchTotalAnswers(passid, tvTotalAnswers);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FillForumBoard();
    }

    private void InsertAnswer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Inserting Answer");
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        String url = appConfig.getUrl() + "forums/insert_answer.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        Toast.makeText(ForumAnswerActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        edtAnswer.setText("");
                        onResume();
                    }

                    if (status == 0) {
                        Toast.makeText(ForumAnswerActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ForumAnswerActivity.this, "Error In Catch Block", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ForumAnswerActivity.this, "Error in Error Listener" + error, Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("questionid", passid);
                map.put("cid", myPreference.GetSession());
                map.put("answer", edtAnswer.getText().toString());
                map.put("answerby", myPreference.getName());
                return map;
            }
        };


        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }

    public void FillForumBoard() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Answers");
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.getUrl() + "forums/select_answer.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    answerList.clear();
                    progressDialog.dismiss();
                    int i;
                    Log.d("response", response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("forum_array");
                    for (i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", jsonObject.getString("id"));
                        map.put("questionid", jsonObject.getString("question_id"));
                        map.put("cid", jsonObject.getString("cid"));
                        map.put("answer", jsonObject.getString("answer"));
                        map.put("answerby", jsonObject.getString("answerby"));
                        map.put("date", jsonObject.getString("date"));
                        answerList.add(map);
                        lvAnswerList.setAdapter(new ForumAnswerAdpter(getApplicationContext(), answerList));
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ForumAnswerActivity.this, "Errror" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("passid", passid);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);

    }

    public void FetchTotalAnswers(final String qid,final TextView tv) {
        String url = appConfig.getUrl() + "forums/fetch_total_answers.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        Log.d("json", jsonObject.getString("row"));
                        tv.setText(jsonObject.getString("row") + " " + "Answers");
                        Log.d("Total Answers", jsonObject.getString("row") + " " + "Answers");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("questionid", qid);
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
