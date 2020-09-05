package com.example.tjg.cahouseadmin.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseadmin.Adapters.ForumQuestionAdapter;
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

public class ForumQuestionActivity extends AppCompatActivity {

    ListView lvForums;
    FloatingActionButton fabForum;
    AppConfig appConfig;
    MyPreference myPreference;
    EditText edtAddQuestion;
    List<HashMap<String, String>> questionList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_question);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvForums = findViewById(R.id.lvForum);
        fabForum = findViewById(R.id.fabForum);
        appConfig = new AppConfig();
        myPreference = new MyPreference(this);

        lvForums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String questionid = questionList.get(position).get("id");
                String cid = questionList.get(position).get("cid");
                String question = questionList.get(position).get("question");
                String questionby = questionList.get(position).get("questionby");
                String date = questionList.get(position).get("date");
                Intent intent = new Intent(ForumQuestionActivity.this, ForumAnswerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", questionid);
                bundle.putString("cid", cid);
                bundle.putString("question", question);
                bundle.putString("questionby", questionby);
                bundle.putString("date", date);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        fabForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.addquestion_single_row, null);
                edtAddQuestion = alertLayout.findViewById(R.id.edtAddQuestion);

                final AlertDialog.Builder builder = new AlertDialog.Builder(ForumQuestionActivity.this);
                builder.setView(alertLayout);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InsertQuestion();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                (alertDialog.getButton(AlertDialog.BUTTON_POSITIVE))
                        .setEnabled(false);
                edtAddQuestion.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(s)) {
                            (alertDialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            (alertDialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FillForumBoard();
    }

    private void InsertQuestion() {

        questionList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Inserting Question");
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        String url = appConfig.getUrl() + "forums/insert_question.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        Toast.makeText(ForumQuestionActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        FillForumBoard();
                    }

                    if (status == 0) {
                        Toast.makeText(ForumQuestionActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ForumQuestionActivity.this, "Error In Catch Block", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ForumQuestionActivity.this, "Error in Error Listener" + error, Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("cid", myPreference.GetSession());
                map.put("question", edtAddQuestion.getText().toString());
                map.put("questionby", myPreference.getName());
                return map;
            }
        };


        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }

    public void FillForumBoard() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Questions");
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.getUrl() + "forums/select_question.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    questionList.clear();
                    progressDialog.dismiss();
                    int i;
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", jsonObject1.getString("id"));
                        map.put("cid", jsonObject1.getString("cid"));
                        map.put("question", jsonObject1.getString("question"));
                        map.put("questionby", jsonObject1.getString("questionby"));
                        map.put("date", jsonObject1.getString("date"));

                        questionList.add(map);
                        lvForums.setAdapter(new ForumQuestionAdapter(getApplicationContext(), questionList));
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(ForumQuestionActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ForumQuestionActivity.this, "Errror" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
