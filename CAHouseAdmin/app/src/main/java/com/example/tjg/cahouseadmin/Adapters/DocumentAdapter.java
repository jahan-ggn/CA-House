package com.example.tjg.cahouseadmin.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tjg.cahouseadmin.Activities.AddDocumentActivity;
import com.example.tjg.cahouseadmin.JavaFiles.Documents;
import com.example.tjg.cahouseadmin.Network.VolleyApplication;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentsViewHolder> {

    Context context;
    List<Documents> documentList;
    AppConfig appConfig;
    int pos;

    public DocumentAdapter(Context context, List<Documents> documentList) {
        this.context = context;
        this.documentList = documentList;
    }

    @Override
    public DocumentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.documents_single_row, null, false);
        appConfig = new AppConfig();
        return new DocumentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DocumentsViewHolder holder, int position) {
        Documents documents = documentList.get(position);

        holder.tvDocumentName.setText(documents.getDocument_name());

    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    class DocumentsViewHolder extends ViewHolder {

        TextView tvDocumentName;
        ImageView ivDeleteDocument, ivEditDocument;

        public DocumentsViewHolder(View itemView) {
            super(itemView);

            tvDocumentName = itemView.findViewById(R.id.tvDocumentName);
            ivDeleteDocument = itemView.findViewById(R.id.ivDeleteDocument);
            ivEditDocument = itemView.findViewById(R.id.ivEditDocument);

            ivDeleteDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are You Sure?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pos = getPosition();
                            DeleteDocument();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.create().show();
                }
            });

            ivEditDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = getPosition();

                    Intent intent = new Intent(context, AddDocumentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("document_id", documentList.get(pos).getId());
                    bundle.putString("document_name", documentList.get(pos).getDocument_name());
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });
        }
    }


    public void DeleteDocument() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Deleting Document");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String url = appConfig.getUrl() + "documentwebservice/delete_document.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");

                    if (status == 1) {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        documentList.remove(pos);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                map.put("document_id", documentList.get(pos).getId());
                return map;
            }
        };

        RetryPolicy newsRetryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(newsRetryPolicy);

        VolleyApplication.getVolleyApplication().getRequestQueue().add(stringRequest);

    }
}
