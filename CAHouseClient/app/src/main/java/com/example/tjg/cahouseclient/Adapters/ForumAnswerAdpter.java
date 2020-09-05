package com.example.tjg.cahouseclient.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tjg.cahouseclient.R;
import com.example.tjg.cahouseclient.Storage.MyPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForumAnswerAdpter extends BaseAdapter {

    Context context;
    List<HashMap<String, String>> ansls = new ArrayList<HashMap<String, String>>();
    MyPreference myPreference;


    //String delID;
    public ForumAnswerAdpter(Context context, List<HashMap<String, String>> ansls) {
        this.context = context;
        this.ansls = ansls;
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
        viewholder.tvClientName.setText("By" + " "+ map.get("answerby"));
        viewholder.tvDate.setText("On" + " " + map.get("date"));
        return convertView;
    }
    static class Viewholder {
        TextView tvAnswer,tvClientName,tvDate;
    }

}
