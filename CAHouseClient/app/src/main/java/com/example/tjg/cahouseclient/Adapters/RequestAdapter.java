package com.example.tjg.cahouseclient.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseclient.Activities.RequestActivity;
import com.example.tjg.cahouseclient.Network.VolleyApplication;
import com.example.tjg.cahouseclient.R;
import com.example.tjg.cahouseclient.Storage.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestAdapter extends BaseAdapter {
    Context context;
    AppConfig appConfig;
    int choice;
    List<HashMap<String, String>> requestedDocList = new ArrayList<HashMap<String, String>>();
    RequestActivity activity;
    public RequestAdapter(Context context, List<HashMap<String, String>> requestedDocList,int choice,RequestActivity activity) {
        this.context = context;
        this.requestedDocList = requestedDocList;
        this.choice = choice;
        this.activity=activity;
        appConfig = new AppConfig();
    }

    @Override
    public int getCount() {
        return requestedDocList.size();
    }

    @Override
    public Object getItem(int position) {
        return requestedDocList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewholder viewholder;
        final HashMap<String, String> map = requestedDocList.get(position);

        if (convertView == null) {
            viewholder = new Viewholder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.request_single_row, null);
            viewholder.tvClientName = convertView.findViewById(R.id.tvAdminName);
            viewholder.tvDocumentName = convertView.findViewById(R.id.tvDocumentName);
            viewholder.btnDone = convertView.findViewById(R.id.btnDone);
            if(choice == 0){
                viewholder.btnDone.setVisibility(View.VISIBLE);
            }

            if(choice == 1){
                viewholder.btnDone.setVisibility(View.GONE);
            }
            viewholder.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetStatus(map.get("request_id"),position);
                }
            });

            convertView.setTag(viewholder);
        } else {
            viewholder = (Viewholder) convertView.getTag();
        }

        viewholder.tvClientName.setText("By: " + map.get("requestby"));
        viewholder.tvDocumentName.setText("For: " + map.get("document_name"));

        this.notifyDataSetChanged();
        return convertView;
    }

    public void SetStatus(final String request_id,final int position){

        String url = appConfig.getUrl() + "documentwebservice/admin_status.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("status") == 1){
                        refreshData(position);
                    }
                    else{
                        Toast.makeText(context, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                Toast.makeText(context, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("request_id", request_id);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);
    }

    public void refreshData(final int position)
    {
        HashMap<String,String> map = requestedDocList.get(position);
        activity.addToFulFilled(map);

        requestedDocList.remove(position);
        notifyDataSetChanged();
    }

    static class Viewholder {
        TextView tvClientName, tvDocumentName;
        Button btnDone;
    }
}

