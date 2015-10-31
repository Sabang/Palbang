package com.sabang.sp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sabang.sp.api.CommentModel;

import java.util.ArrayList;

/**
 * Created by cyc on 2015-11-01.
 */
public class CommentAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<CommentModel> mData;


    public CommentAdapter(Context context, ArrayList<CommentModel> data){
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = data;
    }




    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            ViewHolder vh = new ViewHolder();
            convertView = mInflater.inflate(R.layout.commentitem, parent, false);
            vh.name = (TextView)convertView.findViewById(R.id.comment_userId);
            vh.content = (TextView)convertView.findViewById(R.id.comment_content);
            vh.date = (TextView)convertView.findViewById(R.id.comment_date);

            convertView.setTag(vh);
        }

        CommentModel item = (CommentModel) getItem(position);
        ViewHolder vh = (ViewHolder) convertView.getTag();




        vh.name.setText(item.user);
        vh.content.setText(item.content);
        vh.date.setText(item.date);

        return convertView;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView content;
        public TextView date;
    }

}


