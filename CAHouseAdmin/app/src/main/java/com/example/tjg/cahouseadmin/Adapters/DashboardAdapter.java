package com.example.tjg.cahouseadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tjg.cahouseadmin.R;


public class DashboardAdapter extends BaseAdapter {

    String[] result;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;

    public DashboardAdapter(Context context, String[] prgmNameList, int[] prgmImages) {

        result = prgmNameList;
        this.context = context;
        imageId = prgmImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class Holder {
        TextView tvTitle;
        ImageView ivIcon;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dashboard_single_row, null);
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.ivIcon = convertView.findViewById(R.id.ivIcon);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.tvTitle.setText(result[position]);
        holder.ivIcon.setImageResource(imageId[position]);


        return convertView;
    }

}
