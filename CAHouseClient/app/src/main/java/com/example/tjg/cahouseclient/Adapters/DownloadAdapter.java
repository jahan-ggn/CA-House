package com.example.tjg.cahouseclient.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tjg.cahouseclient.JavaFiles.DownloadImages;
import com.example.tjg.cahouseclient.R;
import com.example.tjg.cahouseclient.Storage.AppConfig;

import java.util.ArrayList;

public class DownloadAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> files_on_server;
    DownloadManager downloadManager;
    AppConfig appConfig;
    public DownloadAdapter(Context context, ArrayList<String> files_on_server){
        this.context = context;
        this.files_on_server = files_on_server;
        appConfig = new AppConfig();
    }
    @Override
    public int getCount() {
        return files_on_server.size();
    }

    @Override
    public Object getItem(int position) {
        return files_on_server.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fetch_document_single_row, null);
            viewHolder.ivDownloadType = convertView.findViewById(R.id.ivDownloadType);
            viewHolder.ivDownload = convertView.findViewById(R.id.ivDownload);
            viewHolder.tvFileName = convertView.findViewById(R.id.tvFileName);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvFileName.setText(files_on_server.get(position));
        DownloadImages downlaodImages = new DownloadImages();
        downlaodImages.setFileLogo(files_on_server.get(position), viewHolder.ivDownloadType);
        viewHolder.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected_file = files_on_server.get(position);
                downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(appConfig.getUrl() + "upload/fileuploads/uploads/" + selected_file);
                DownloadManager.Request DownloadRequest = new DownloadManager.Request(uri);
                DownloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(DownloadRequest);
            }
        });
        return convertView;
    }

    static class ViewHolder{
        ImageView ivDownloadType, ivDownload;
        TextView tvFileName;
    }
}
