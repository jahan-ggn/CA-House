package com.example.tjg.cahouseadmin.Adapters;

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
import com.example.tjg.cahouseadmin.Network.VolleyApplication;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;
import com.example.tjg.cahouseadmin.Storage.MyPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ForumAnswerAdpter extends BaseAdapter {

    Context context;
    List<HashMap<String, String>> ansls = new ArrayList<HashMap<String, String>>();
    MyPreference myPreference;
    AppConfig appConfig;


    public ForumAnswerAdpter(Context context, List<HashMap<String, String>> ansls) {
        this.context = context;
        this.ansls = ansls;
        appConfig = new AppConfig();
        myPreference = new MyPreference(context);
    }

    @Override
    public int getCount() {
        return ansls.size();
    }

    @Override
    public Object getItem(int i) {
        return ansls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Viewholder viewholder;
        final HashMap<String, String> map = ansls.get(position);

        if (convertView == null) {
            viewholder = new Viewholder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.answer_single_row, null);
            viewholder.tvAnswer = convertView.findViewById(R.id.tvAnswer);
            viewholder.tvClientName = convertView.findViewById(R.id.tvAdminName);
            viewholder.tvDate = convertView.findViewById(R.id.tvDate);
            convertView.setTag(viewholder);
        } else {
            viewholder = (Viewholder) convertView.getTag();
        }

        viewholder.tvAnswer.setText(map.get("answer"));
        viewholder.tvClientName.setText("By" + " " + map.get("answerby"));
        viewholder.tvDate.setText("On" + " " + map.get("date"));
        return convertView;
    }
        static class Viewholder {
        TextView tvAnswer,tvClientName,tvDate, tvTotalAnswers;
    }

}
