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
public class ListviewAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<RoomListviewitem> mData;
    private ArrayList<BoardListviewitem> mData2;

    public ListviewAdapter(Context context, ArrayList<RoomListviewitem> data){
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
            convertView = mInflater.inflate(R.layout.roomitem, parent, false);
            vh.icon = (ImageView)convertView.findViewById(R.id.room_imageview);
            vh.price = (TextView)convertView.findViewById(R.id.room_textview1);
            vh.area = (TextView)convertView.findViewById(R.id.room_textview2);
            convertView.setTag(vh);
        }

        RoomListviewitem item = (RoomListviewitem) getItem(position);
        ViewHolder vh = (ViewHolder) convertView.getTag();


        vh.icon.setImageResource(item.icon);
        vh.price.setText(item.security+"/"+item.monthly);
        vh.area.setText(""+item.area);


        return convertView;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView price;
        public TextView area;

    }
}
