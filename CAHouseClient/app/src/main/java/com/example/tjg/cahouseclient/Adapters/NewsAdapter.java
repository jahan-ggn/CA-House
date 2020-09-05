package com.example.tjg.cahouseclient.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tjg.cahouseclient.JavaFiles.News;
import com.example.tjg.cahouseclient.R;

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    Context context;
    List<News> newsList;

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
        holder.tvNewsDate.setText(newsActivity.getNews_date());
        holder.tvNewsTime.setText(newsActivity.getNews_time());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tvNewsTitle, tvNewsDescription, tvNewsTime, tvNewsDate;

        public NewsViewHolder(View itemView) {
            super(itemView);

            tvNewsTitle = itemView.findViewById(R.id.tvNewsTitle);
            tvNewsDescription = itemView.findViewById(R.id.tvNewsDescription);
            tvNewsDate = itemView.findViewById(R.id.tvNewsDate);
            tvNewsTime = itemView.findViewById(R.id.tvNewsTime);
        }
    }
}
