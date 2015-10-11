package com.sabang.sp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.sabang.sp.api.RoomModel;
import com.sabang.sp.api.VolleySingleton;

import java.util.List;

/**
 * Created by cyc on 2015-08-19.
 */
public class ListviewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<RoomModel> mData;
    private Context context;

    public ListviewAdapter(Context context, List<RoomModel> data) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if (convertView == null) {
            ViewHolder vh = new ViewHolder();
            convertView = mInflater.inflate(R.layout.roomitem, parent, false);
            vh.icon = (NetworkImageView) convertView.findViewById(R.id.room_imageview);
            vh.price = (TextView) convertView.findViewById(R.id.room_textview1);
            vh.area = (TextView) convertView.findViewById(R.id.room_textview2);
            convertView.setTag(vh);
        }

        RoomModel item = (RoomModel) getItem(position);
        ViewHolder vh = (ViewHolder) convertView.getTag();


        //need fix random image to roomlist
        //vh.icon.setImageBitmap(null);

        vh.icon.setImageUrl(item.images[0], VolleySingleton.getInstance().getImageLoader());

        vh.price.setText(item.securityDeposit + "/" + item.monthPrice);
        vh.area.setText("" + item.area);


        return convertView;
    }

    private static class ViewHolder {
        public NetworkImageView icon;
        public TextView price;
        public TextView area;
    }
}
