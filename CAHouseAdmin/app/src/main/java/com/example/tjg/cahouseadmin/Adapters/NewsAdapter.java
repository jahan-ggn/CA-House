package com.example.tjg.cahouseadmin.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseadmin.Activities.AddNewsActivity;
import com.example.tjg.cahouseadmin.JavaFiles.FetchClients;
import com.example.tjg.cahouseadmin.JavaFiles.News;
import com.example.tjg.cahouseadmin.Network.VolleyApplication;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    Context context;
    List<News> newsList;
    AppConfig appConfig = new AppConfig();
    int pos;

    public NewsAdapter(Context context, List<News> newsList) {

        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_single_row, null, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News newsActivity = newsList.get(position);


        holder.tvNewsTitle.setText(newsActivity.getNews_title());
        holder.tvNewsDescription.setText(newsActivity.getNews_description());
        holder.tvNewsStatus.setText(newsActivity.getNews_status());
        holder.tvNewsDate.setText(newsActivity.getNews_date());
        holder.tvNewsTime.setText(newsActivity.getNews_time());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tvNewsTitle, tvNewsDescription, tvNewsTime, tvNewsDate, tvNewsStatus, tvNewsPopUp;

        public NewsViewHolder(View itemView) {
            super(itemView);

            tvNewsTitle = itemView.findViewById(R.id.tvNewsTitle);
            tvNewsDescription = itemView.findViewById(R.id.tvNewsDescription);
            tvNewsDate = itemView.findViewById(R.id.tvNewsDate);
            tvNewsTime = itemView.findViewById(R.id.tvNewsTime);
            tvNewsStatus = itemView.findViewById(R.id.tvNewsStatus);
            tvNewsPopUp = itemView.findViewById(R.id.tvNewsPopUp);
            tvNewsPopUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, tvNewsPopUp);
                    popupMenu.getMenuInflater().inflate(R.menu.news_popup_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.item_edit) {
                                pos = getPosition();

                                Intent intent = new Intent(context, AddNewsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("news_id", newsList.get(pos).getNews_id());
                                bundle.putString("news_title", newsList.get(pos).getNews_title());
                                bundle.putString("news_description", newsList.get(pos).getNews_description());
                                bundle.putString("news_status", newsList.get(pos).getNews_status());
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }

                            if (id == R.id.item_delete) {
                                pos = getPosition();

                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                builder.setTitle("Confirmation");
                                builder.setMessage("Are You Sure You Want To Delete The News?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DeleteNews();
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                builder.create().show();
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        private void DeleteNews() {

            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Deleting News");
            progressDialog.setTitle("Please Wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();

            String url = appConfig.getUrl() + "newswebservice/delete_news.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt("status");

                        if (status == 1) {
                            Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            newsList.remove(pos);
                            notifyDataSetChanged();
                        }

                        if (status == 0) {
                            Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("news_id", newsList.get(pos).getNews_id());
                    return map;
                }
            };

            RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(retryPolicy);

            VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);

        }
    }
}

