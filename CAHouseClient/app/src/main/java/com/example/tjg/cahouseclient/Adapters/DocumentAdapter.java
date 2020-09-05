package com.example.tjg.cahouseclient.Adapters;

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
import com.example.tjg.cahouseclient.JavaFiles.Documents;
import com.example.tjg.cahouseclient.Network.VolleyApplication;
import com.example.tjg.cahouseclient.R;
import com.example.tjg.cahouseclient.Storage.AppConfig;
import com.example.tjg.cahouseclient.JavaFiles.Documents;

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

        }
    }
}