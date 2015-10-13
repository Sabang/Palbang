package com.sabang.sp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cyc on 2015-08-19.
 */
public class ListviewAdapter2 extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<BoardData> mData;


    public ListviewAdapter2(Context context, ArrayList<BoardData> data){
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
            convertView = mInflater.inflate(R.layout.boarditem, parent, false);
            vh.icon = (ImageView)convertView.findViewById(R.id.board_imageview);
            vh.title = (TextView)convertView.findViewById(R.id.board_textview0);
            vh.name = (TextView)convertView.findViewById(R.id.board_textview1);
            vh.price = (TextView)convertView.findViewById(R.id.board_textview2);
            vh.date = (TextView)convertView.findViewById(R.id.board_textview3);

            convertView.setTag(vh);
        }

        BoardData item = (BoardData) getItem(position);
        ViewHolder vh = (ViewHolder) convertView.getTag();

        try {
            vh.icon.setImageResource(item.icon);
        }
        catch(OutOfMemoryError e){
            vh.icon.setImageBitmap(null);
        }
        vh.title.setText(""+item.title);
        vh.name.setText(""+item.name);
        vh.price.setText(""+item.price+" 만");
        vh.date.setText(item.date);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView name;
        public TextView price;
        public TextView date;
    }
}
