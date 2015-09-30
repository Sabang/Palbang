package com.sabang.sp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sabang.sp.api.RoomModel;

import java.io.Serializable;

public class RoomActivity extends AppCompatActivity implements View.OnTouchListener, OnClickListener {
    ImageView mapimageview;
    float xAtDown;
    float xAtUp;
    Button listbtn;
    int location;


    public int[] mRes = new int[]{
            R.drawable.room1, R.drawable.room2, R.drawable.room3, R.drawable.room4


    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapimageview = (ImageView) findViewById(R.id.mapimageview);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ImageAdapter adapter2 = new ImageAdapter();
        viewPager.setAdapter(adapter2);

        mapimageview.setScaleType(ImageView.ScaleType.FIT_XY);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);

        Intent intent = getIntent();

        Serializable temp = (Serializable) intent.getSerializableExtra("roomModel");

        RoomModel room = (RoomModel) temp;

        location = ((RoomModel) temp).area;
        String price = "" + ((RoomModel) temp).securityDeposit + "/" + ((RoomModel) temp).monthPrice;

        changeimage(location);
        TextView roomPrice = (TextView) findViewById(R.id.room_price);
        roomPrice.setText(price);
    }

    // View.OnTouchListener의 abstract method
    // flipper 터지 이벤트 리스너
    class ImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mRes.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(mRes[position]);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 터치 이벤트가 일어난 뷰가 ViewFlipper가 아니면 return
        return true;
    }

    public void changeimage(int x) {
        switch (x) {
            case 0:
                mapimageview.setImageResource(R.drawable.first_zone_map);
                break;
            case 1:
                mapimageview.setImageResource(R.drawable.second_zone_map);
                break;
            case 2:
                mapimageview.setImageResource(R.drawable.third_zone_map);
                break;
            case 3:
                mapimageview.setImageResource(R.drawable.fourth_zone_map);
                break;
            case 4:
                mapimageview.setImageResource(R.drawable.fifth_zone_map);
                break;
            case 5:
                mapimageview.setImageResource(R.drawable.sixth_zone_map);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}