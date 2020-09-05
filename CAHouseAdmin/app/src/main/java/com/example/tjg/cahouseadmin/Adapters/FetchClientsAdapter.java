package com.example.tjg.cahouseadmin.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.tjg.cahouseadmin.JavaFiles.FetchClients;
import com.example.tjg.cahouseadmin.Network.VolleyApplication;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchClientsAdapter extends RecyclerView.Adapter<FetchClientsAdapter.FetchClientsViewHolder> {

    Context context;
    List<FetchClients> clientsList;
    AppConfig appConfig = new AppConfig();
    int pos;

    public FetchClientsAdapter(Context context, List<FetchClients> clientsList) {
        this.context = context;
        this.clientsList = clientsList;
    }

    @Override
    public FetchClientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fetch_clients_single_row, null, false);
        return new FetchClientsAdapter.FetchClientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FetchClientsViewHolder holder, int position) {
        FetchClients fetchClientsAdapter = clientsList.get(position);

        holder.tvName.setText(fetchClientsAdapter.getName());
        holder.tvEmail.setText(fetchClientsAdapter.getEmail());
        holder.tvMobile.setText(fetchClientsAdapter.getMobile());
    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }

    public class FetchClientsViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvEmail, tvMobile, tvPopUp;

        public FetchClientsViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvPopUp = itemView.findViewById(R.id.tvPopUp);

            tvPopUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, tvPopUp);
                    popupMenu.getMenuInflater().inflate(R.menu.fetch_client_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.item_delete) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                builder.setTitle("Confirmation");
                                builder.setMessage("Are You Sure You Want To Delete The Client?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DeleteNews();
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
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
            progressDialog.setMessage("Deleting Client");
            progressDialog.setTitle("Please Wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();

            String url = appConfig.getUrl() + "loginwebservice/delete_client.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt("status");

                        if (status == 1) {
                            Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            clientsList.remove(pos);
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
                    map.put("cid", clientsList.get(pos).getId());
                    Log.e("cid", clientsList.get(pos).getId());
                    return map;
                }
            };

            RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(retryPolicy);

            VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);

        }
    }
}

