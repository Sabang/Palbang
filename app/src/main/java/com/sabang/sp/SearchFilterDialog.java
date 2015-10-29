package com.sabang.sp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by cyc on 2015-09-06.
 */
public class SearchFilterDialog extends Dialog
{
    private Button acceptButton;
    private Button cancelButton;
    private ImageView firstImageView;
    private ImageView secondImageView;
    private ImageView thirdImageView;
    private ImageView fourthImageView;
    private ImageView fifthImageView;
    private ImageView sixthImageView;
    private Spinner spinner_security_min;
    private Spinner spinner_security_max;
    private Spinner spinner_monthly_min;
    private Spinner spinner_monthly_max;
    private Context context;
    SearchFilterData data;

    // this is your interface for what you want to do on the calling activity
    public interface ICustomDialogEventListener {
        public void customDialogEvent();
    }

    private ICustomDialogEventListener onCustomDialogEventListener;

    // In the constructor, you set the callback
    public SearchFilterDialog(Context context, SearchFilterData data,  ICustomDialogEventListener onCustomDialogEventListener) {
        super(context);
        this.context =context;
        this.data = data;
        this.onCustomDialogEventListener = onCustomDialogEventListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_search_filter);


        firstImageView = (ImageView)findViewById(R.id.first_zone_map);
        secondImageView = (ImageView)findViewById(R.id.second_zone_map);
        thirdImageView = (ImageView)findViewById(R.id.third_zone_map);
        fourthImageView = (ImageView)findViewById(R.id.fourth_zone_map);
        fifthImageView = (ImageView)findViewById(R.id.fifth_zone_map);
        sixthImageView = (ImageView)findViewById(R.id.sixth_zone_map);






        spinner_security_min = (Spinner) this.findViewById(R.id.dialogfragment_spinner_security_min);
        spinner_security_max = (Spinner) this.findViewById(R.id.dialogfragment_spinner_security_max);
        spinner_monthly_min = (Spinner) this.findViewById(R.id.dialogfragment_spinner_monthly_min);
        spinner_monthly_max = (Spinner) this.findViewById(R.id.dialogfragment_spinner_monthly_max);
        spinner_security_min.setAdapter(ArrayAdapter.createFromResource(
                this.getContext(), R.array.security_money, android.R.layout.simple_spinner_dropdown_item));
        spinner_security_max.setAdapter(ArrayAdapter.createFromResource(
                this.getContext(), R.array.security_money, android.R.layout.simple_spinner_dropdown_item));
        spinner_monthly_min.setAdapter(ArrayAdapter.createFromResource(
                this.getContext(), R.array.monthly_money, android.R.layout.simple_spinner_dropdown_item));
        spinner_monthly_max.setAdapter(ArrayAdapter.createFromResource(
                this.getContext(), R.array.monthly_money, android.R.layout.simple_spinner_dropdown_item));


        ImageView map = (ImageView)findViewById(R.id.empty_map);
        map.setOnTouchListener(mTouchListener);


        getDataCheckBox();
        getDataSpinner();





        acceptButton = (Button) this.findViewById(R.id.dialogfragment_acceptbtn);
        cancelButton = (Button) this.findViewById(R.id.dialogfragment_canceltbtn);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataCheckBox();
                setDataSpinner();
                onCustomDialogEventListener.customDialogEvent();

                dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }



    public void getDataSpinner(){
        String[] array_security = context.getResources().getStringArray(R.array.security_money);
        String[] array_monthly = context.getResources().getStringArray(R.array.monthly_money);

        if(data.isSecurity_min == false) {
            spinner_security_min.setSelection(0);
        }
        else{
            for(int i=0;i<array_security.length;i++){
                if(data.security_min.equals(array_security[i]))
                    spinner_security_min.setSelection(i);
            }
        }

        if(data.isSecurity_max == false) {
            spinner_security_max.setSelection(0);
        }
        else{
            for(int i=0;i<array_security.length;i++){
                if(data.security_max.equals(array_security[i]))
                    spinner_security_max.setSelection(i);
            }
        }

        if(data.isMonthly_min == false) {
            spinner_monthly_min.setSelection(0);
        }
        else{
            for(int i=0;i<array_monthly.length;i++){
                if(data.montly_min.equals(array_monthly[i]))
                    spinner_monthly_min.setSelection(i);
            }
        }

        if(data.isMonthly_max == false) {
            spinner_monthly_max.setSelection(0);
        }
        else{
            for(int i=0;i<array_monthly.length;i++){
                if(data.montly_max.equals(array_monthly[i]))
                    spinner_monthly_max.setSelection(i);
            }
        }

    }

    public void setDataSpinner(){
        String securityFirstElement = context.getResources().getStringArray(R.array.security_money)[0];
        String monthlyFristElement = context.getResources().getStringArray(R.array.monthly_money)[0];

        String security_min = (String) spinner_security_min.getSelectedItem();
        String security_max = (String) spinner_security_max.getSelectedItem();
        String monthly_min = (String) spinner_monthly_min.getSelectedItem();
        String monthly_max = (String) spinner_monthly_max.getSelectedItem();

        if(security_min.equals(securityFirstElement)){
            data.isSecurity_min = false;
        }
        else{
            data.isSecurity_min = true;
            data.security_min = security_min;
        }//end else
        if(security_max.equals(securityFirstElement)){
            data.isSecurity_max = false;
        }
        else{
            data.isSecurity_max = true;
            data.security_max = security_max;
        }//end else

        if(monthly_min.equals(monthlyFristElement)){
            data.isMonthly_min = false;
        }
        else{
            data.isMonthly_min = true;
            data.montly_min = monthly_min;
        }//end else
        if(monthly_max.equals(monthlyFristElement)){
            data.isMonthly_max = false;
        }
        else{
            data.isMonthly_max = true;
            data.montly_max = monthly_max;
        }//end else
    }//end func



    public void getDataCheckBox(){



        firstImageView.setVisibility((data.check_A) ? View.VISIBLE : View.INVISIBLE);
        secondImageView.setVisibility((data.check_B) ? View.VISIBLE : View.INVISIBLE);
        thirdImageView.setVisibility((data.check_C) ? View.VISIBLE : View.INVISIBLE);
        fourthImageView.setVisibility((data.check_D) ? View.VISIBLE : View.INVISIBLE);
        fifthImageView.setVisibility((data.check_E) ? View.VISIBLE : View.INVISIBLE);
        sixthImageView.setVisibility((data.check_F) ? View.VISIBLE : View.INVISIBLE);
    }

    public void setDataCheckBox(){
        data.check_A = (firstImageView.getVisibility() == View.VISIBLE);
        data.check_B = (secondImageView.getVisibility() == View.VISIBLE);
        data.check_C = (thirdImageView.getVisibility() == View.VISIBLE);
        data.check_D = (fourthImageView.getVisibility() == View.VISIBLE);
        data.check_E = (fifthImageView.getVisibility() == View.VISIBLE);
        data.check_F = (sixthImageView.getVisibility() == View.VISIBLE);
    }



    //mapping
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int[] color = {Color.argb(255,255,0,0),Color.argb(255,255,0,208),
                    Color.argb(255,150,0,255),Color.argb(255,0,6,255),
                    Color.argb(255,0,186,255),Color.argb(255,0,255,24)};

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
                    for(int i=0;i<6;i++){
                        if(closeMatch(color[i], touchColor, tolerance)){
                            switch(i){
                                case 0:
                                    if(firstImageView.getVisibility() == View.VISIBLE)
                                        firstImageView.setVisibility(View.INVISIBLE);
                                    else
                                        firstImageView.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    if(secondImageView.getVisibility() == View.VISIBLE)
                                        secondImageView.setVisibility(View.INVISIBLE);
                                    else
                                        secondImageView.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    if(thirdImageView.getVisibility() == View.VISIBLE)
                                        thirdImageView.setVisibility(View.INVISIBLE);
                                    else
                                        thirdImageView.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    if(fourthImageView.getVisibility() == View.VISIBLE)
                                        fourthImageView.setVisibility(View.INVISIBLE);
                                    else
                                        fourthImageView.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    if(fifthImageView.getVisibility() == View.VISIBLE)
                                        fifthImageView.setVisibility(View.INVISIBLE);
                                    else
                                        fifthImageView.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    if(sixthImageView.getVisibility() == View.VISIBLE)
                                        sixthImageView.setVisibility(View.INVISIBLE);
                                    else
                                        sixthImageView.setVisibility(View.VISIBLE);
                                    break;
                            }

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
        if(x<0) x=0;
        if(y<0) y=0;
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