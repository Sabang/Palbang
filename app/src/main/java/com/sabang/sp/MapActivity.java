package com.sabang.sp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MapActivity extends AppCompatActivity {

    final int[] color = {Color.argb(255,255,0,0),Color.argb(255,255,0,208),
                        Color.argb(255,150,0,255),Color.argb(255,0,6,255),
                        Color.argb(255,0,186,255),Color.argb(255,0,255,24)};
    ImageView map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);

        map = (ImageView)findViewById(R.id.map);
        map.setOnTouchListener(mTouchListener);



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //mapping
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            final int action = event.getAction();
            // (1)
            final int evX = (int) event.getX();
            final int evY = (int) event.getY();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    int touchColor = getHotspotColor(R.id.map_mask, evX, evY);
                    int tolerance = 25;
                    // (3)
                    //for test only
                    String str = "select : ";
                    for(int i=0;i<6;i++){
                        if(closeMatch(color[i], touchColor, tolerance)){
                            str+= (i+1)+"area";


                            Toast toast = Toast.makeText(getApplicationContext(),
                                    str, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();


                            Intent intent = getIntent();
                            intent.putExtra("area", i);

                            startActivity(intent);

                            finish();

                            break;
                        }
                    }

                    break;
            } // end switch
            return true;
        }
    };

    public int getHotspotColor (int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById (hotspotId);
        img.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
        img.setDrawingCacheEnabled(false);
        int temp = hotspots.getPixel(x, y);
        //release bitmap, don't waste memory.
        hotspots.recycle();

        return temp;
    }
    public boolean closeMatch (int color1, int color2, int tolerance) {
        if ((int) Math.abs (Color.red(color1) - Color.red (color2)) > tolerance )
            return false;
        if ((int) Math.abs (Color.green (color1) - Color.green (color2)) > tolerance )
            return false;
        if ((int) Math.abs (Color.blue (color1) - Color.blue (color2)) > tolerance )
            return false;
        return true;
    } // end match

}
