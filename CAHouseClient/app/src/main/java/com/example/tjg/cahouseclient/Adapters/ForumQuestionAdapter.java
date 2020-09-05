package com.example.tjg.cahouseclient.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ForumQuestionAdapter extends BaseAdapter {

    Context context;
    List<HashMap<String, String>> ls = new ArrayList<HashMap<String, String>>();
    AppConfig appConfig = new AppConfig();

    public ForumQuestionAdapter(Context context, List<HashMap<String, String>> ls) {
        this.context = context;
        this.ls = ls;
    }

    @Override
    public int getCount() {
        return ls.size();
    }

    @Override
    public Object getItem(int i) {
        return ls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        final Viewholder viewholder;
        final HashMap<String, String> map = ls.get(position);

        if (convertView == null) {
            viewholder = new Viewholder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.forum_single_row, null);
            viewholder.tvQuestion = convertView.findViewById(R.id.tvQuestion);
            viewholder.tvClientName = convertView.findViewById(R.id.tvAdminName);
            viewholder.tvDate = convertView.findViewById(R.id.tvDate);
            viewholder.tvTotalAnswers = convertView.findViewById(R.id.tvTotalAnswers);
            convertView.setTag(viewholder);
        } else {
            viewholder = (Viewholder) convertView.getTag();
        }
        viewholder.tvQuestion.setText(map.get("question"));
        viewholder.tvClientName.setText("By "+map.get("questionby"));
        viewholder.tvDate.setText("Date " + map.get("date"));
        FetchTotalAnswers(map.get("id"), viewholder.tvTotalAnswers);
        this.notifyDataSetChanged();
        return convertView;


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

    static class Viewholder {
        TextView tvQuestion, tvClientName, tvDate, tvTotalAnswers;
    }
}
