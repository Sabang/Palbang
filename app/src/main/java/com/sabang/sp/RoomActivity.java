package com.sabang.sp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import com.android.volley.toolbox.NetworkImageView;
import com.sabang.sp.api.RoomModel;
import com.sabang.sp.api.VolleySingleton;

import java.io.Serializable;

public class RoomActivity extends AppCompatActivity implements View.OnTouchListener, OnClickListener {
    ImageView mapimageview;
    float xAtDown;
    float xAtUp;
    Button listbtn;
    int location;


    String[] mRes;
    /*public int[] mRes = new int[]{
            R.drawable.room1, R.drawable.room2, R.drawable.room3, R.drawable.room4


    };*/

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


        Intent intent = getIntent();

        Serializable temp = (Serializable) intent.getSerializableExtra("roomModel");

        RoomModel room = (RoomModel) temp;

        location = room.area;
        changeimage(location);


        TextView roomPrice = (TextView) findViewById(R.id.room_price);
        String price = "" + room.securityDeposit + "/" + ((RoomModel) temp).monthPrice;
        roomPrice.setText(price);

        //제곱미터 ÷ 3.3058 = 평수
        TextView roomSize = (TextView) findViewById(R.id.room_size);
        double m = Double.parseDouble(String.format("%.1f", room.size*3.3058));
        String size = "" + room.size+"평("+m+"m²)";
        roomSize.setText(size);

        //계약기간
        TextView roomTerm = (TextView) findViewById(R.id.room_term);
        roomTerm.setText(room.term);

        //층수
        TextView floor = (TextView) findViewById(R.id.room_floor);
        int iFloor = room.floor;
        if(iFloor == -1){
            floor.setText("지하 1층");
        }
        else{
            floor.setText(iFloor +"층");
        }

        //관리비
        TextView roomManagement = (TextView) findViewById(R.id.room_management);
        roomManagement.setText(room.managementCost+"");

        //구조
        TextView roomStructure = (TextView) findViewById(R.id.room_structure);
        String structure = "";
        if(room.veranda == 1)   structure += "베란다ㅇ, ";
        if(room.kitchen == 1)   structure += "부엌분리형, ";
        else                    structure += "부엌일체형, ";
        if(room.twoRoom == 1)   structure += "투룸, ";
        structure = removeLastTwo(structure);
        roomStructure.setText(structure);

        //가구
        TextView roomFur = (TextView) findViewById(R.id.room_furniture);
        String furniture = "";
        if(room.bed == 1)   furniture += "침대, ";
        if(room.desk == 1)  furniture += "책상, ";

        furniture = removeLastTwo(furniture);
        roomFur.setText(furniture);

        //가전기기
        TextView roomGadget = (TextView) findViewById(R.id.room_gadget);
        String gadget = "";
        if(room.tv == 1)            gadget += "TV, ";
        if(room.microwave == 1)     gadget += "전자레인지, ";
        if(room.airConditioner == 1)gadget += "에어컨, ";
        if(room.washer == 1)        gadget += "세탁기, ";
        if(room.fireType == 1)      gadget += "가스레인지, ";
        else                        gadget += "쿡탑(인덕션), ";
        gadget += "냉장고, ";
        gadget = removeLastTwo(gadget);
        roomGadget.setText(gadget);

        //상세정보
        TextView roomInfo = (TextView) findViewById(R.id.room_information);
        roomInfo.setText(room.detail);


        mRes = room.images;

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

        //전화하기
        Button call_button = (Button)findViewById(R.id.room_call);
        call_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:031-409-0500")));
            }
        });

    }

    public String removeLastTwo(String str){
        //맨뒤쉼표없애기 ㅜ
        int length = str.length();
        if(length != 0)
            str = str.substring(0,length-2);
        return str;
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
            /*
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(mRes[position]);*/
            NetworkImageView imageView = new NetworkImageView(getApplicationContext());
            imageView.setImageUrl(mRes[position], VolleySingleton.getInstance().getImageLoader());
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
            case 1:
                mapimageview.setImageResource(R.drawable.first_zone_map);
                break;
            case 2:
                mapimageview.setImageResource(R.drawable.second_zone_map);
                break;
            case 3:
                mapimageview.setImageResource(R.drawable.third_zone_map);
                break;
            case 4:
                mapimageview.setImageResource(R.drawable.fourth_zone_map);
                break;
            case 5:
                mapimageview.setImageResource(R.drawable.fifth_zone_map);
                break;
            case 6:
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