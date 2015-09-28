package com.sabang.sp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cyc on 2015-08-19.
 */
public class ListviewAdapter2 extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<BoardListviewitem> mData;

    public ListviewAdapter2(Context context, ArrayList<BoardListviewitem> data){
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
            vh.price = (TextView)convertView.findViewById(R.id.board_textview1);
            vh.date = (TextView)convertView.findViewById(R.id.board_textview2);
            vh.back = (LinearLayout)convertView.findViewById(R.id.boarditem_background);
            convertView.setTag(vh);
        }

        BoardListviewitem item = (BoardListviewitem) getItem(position);
        ViewHolder vh = (ViewHolder) convertView.getTag();


        vh.icon.setImageResource(item.icon);
        vh.price.setText(item.name + " / " + item.price);
        vh.date.setText(item.year + "." + item.month + "." + item.day);
        if(item.state ==1){
            vh.back.setBackgroundColor(Color.rgb(227,255,220));
        }
        else if(item.state ==2){
            vh.back.setBackgroundColor(Color.rgb(255,240,240));
        }
        else{
            vh.back.setBackgroundColor(Color.rgb(255,255,210));
        }
        return convertView;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView price;
        public TextView date;
        public LinearLayout back;
    }
}
