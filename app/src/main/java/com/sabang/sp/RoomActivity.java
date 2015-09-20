package com.sabang.sp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.Serializable;

public class RoomActivity extends AppCompatActivity implements View.OnTouchListener, OnClickListener {
    ViewFlipper flipper;
    ImageView mapimageview;
    float xAtDown;
    float xAtUp;
    Button listbtn;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapimageview = (ImageView) findViewById(R.id.mapimageview);
        flipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        flipper.setOnTouchListener(this);
        mapimageview.setScaleType(ImageView.ScaleType.FIT_XY);
        // ViewFlipper에 동적으로 child view 추가
        //  TextView tv = new TextView(this)
        // tv.setText("View 4\nDynamically added");
        // tv.setTextColor(Color.CYAN);
        // flipper.addView(tv);


        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);

        Intent intent = getIntent();

        Serializable temp = (Serializable) intent.getSerializableExtra("roomItem");

        RoomListviewitem room = (RoomListviewitem)temp;


        String str = ""+((RoomListviewitem) temp).monthly;
        Toast toast = Toast.makeText(getApplicationContext(),
                str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();


        TextView roomPrice = (TextView)findViewById(R.id.room_price);
        roomPrice.setText(str);

}

    // View.OnTouchListener의 abstract method
    // flipper 터지 이벤트 리스너
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 터치 이벤트가 일어난 뷰가 ViewFlipper가 아니면 return
        if(v != flipper) return false;
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            xAtDown = event.getX(); // 터치 시작지점 x좌표 저장
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            xAtUp = event.getX();   // 터치 끝난지점 x좌표 저장
            if( xAtUp < xAtDown ) {
                // 왼쪽 방향 에니메이션 지정
                flipper.setInAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.push_left_in));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.push_left_out));
                // 다음 view 보여줌
                flipper.showNext();
            }
            else if (xAtUp > xAtDown){
                // 오른쪽 방향 에니메이션 지정
                flipper.setInAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.push_right_in));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.push_right_out));
                // 전 view 보여줌
                flipper.showPrevious();
            }
        }
        return true;
    }
/*    public void changeimage(){
            int i = 5;
            switch(i){
                case 1:
                    mapimageview.setImageResource(R.drawable.first_zone_map.png);
                    break;
                case 2:
                    mapimageview.setImageResource(R.drawable.second_zone_map.png);
                    break;
                case 3:
                    mapimageview.setImageResource(R.drawable.third_zone_map.png);
                    break;
                case 4:
                    mapimageview.setImageResource(R.drawable.fourth_zone_map.png);
                    break;
                case 5:
                    mapimageview.setImageResource(R.drawable.fifth_zone_map.png);
                    break;
                case 6:
                    mapimageview.setImageResource(R.drawable.sixth_zone_map.png);
                    break;
            }
        }*/
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
