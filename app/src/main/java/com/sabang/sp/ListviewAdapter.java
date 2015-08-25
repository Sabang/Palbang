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
    private LayoutInflater inflater;
    private ArrayList<RoomListviewitem> data;
    private int layout;

    public ListviewAdapter(Context context, int layout, ArrayList<RoomListviewitem> data){
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(layout, parent, false);
        }

        RoomListviewitem roomlistviewitem = data.get(position);

        ImageView icon = (ImageView)convertView.findViewById(R.id.imageview_room);
        icon.setImageResource(roomlistviewitem.getIcon());

        TextView name = (TextView)convertView.findViewById(R.id.textview_room);
        name.setText(roomlistviewitem.getName());

        return convertView;
    }
}
