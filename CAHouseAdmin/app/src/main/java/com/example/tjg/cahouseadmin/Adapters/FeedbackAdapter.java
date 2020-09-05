package com.example.tjg.cahouseadmin.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tjg.cahouseadmin.JavaFiles.Feedback;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.AppConfig;

import java.util.List;


public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>{
    Context context;
    List<Feedback> feedList;

    public FeedbackAdapter(Context context, List<Feedback> feedList) {

        this.context = context;
        this.feedList = feedList;
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.feedback_single_row, null, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {
        Feedback feedback = feedList.get(position);

        holder.tvFeedbackName.setText(feedback.getFeedname());
        holder.tvFeedbackMsg.setText(feedback.getFeedmsg());
        holder.tvFeedbackDate.setText(feedback.getFeeddate());
        holder.tvFeedbackTime.setText(feedback.getFeedtime());
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class FeedbackViewHolder extends RecyclerView.ViewHolder{

        TextView tvFeedbackName, tvFeedbackMsg, tvFeedbackDate, tvFeedbackTime;
        public FeedbackViewHolder(View itemView) {
            super(itemView);
            tvFeedbackName = itemView.findViewById(R.id.tvFeedbackName);
            tvFeedbackMsg = itemView.findViewById(R.id.tvFeedbackMsg);
            tvFeedbackDate = itemView.findViewById(R.id.tvFeedbackDate);
            tvFeedbackTime = itemView.findViewById(R.id.tvFeedbackTime);
        }
    }
}
