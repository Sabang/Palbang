package com.sabang.sp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by cyc on 2015-09-06.
 */
public class testDialog extends Dialog
{
    private Button acceptButton;
    private Button cancelButton;
    private ImageView imageViewArea0;
    private ImageView imageViewArea1;
    private ImageView imageViewArea2;
    private ImageView imageViewArea3;
    private ImageView imageViewArea4;
    private ImageView imageViewArea5;
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
    public testDialog(Context context, SearchFilterData data,  ICustomDialogEventListener onCustomDialogEventListener) {
        super(context);
        this.context =context;
        this.data = data;
        this.onCustomDialogEventListener = onCustomDialogEventListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_test);
        getDataCheckBox();


        imageViewArea0 = (ImageView)findViewById(R.id.first_zone_map);
        imageViewArea1 = (ImageView)findViewById(R.id.second_zone_map);
        imageViewArea2 = (ImageView)findViewById(R.id.third_zone_map);
        imageViewArea3 = (ImageView)findViewById(R.id.fourth_zone_map);
        imageViewArea4 = (ImageView)findViewById(R.id.fifth_zone_map);
        imageViewArea5 = (ImageView)findViewById(R.id.sixth_zone_map);


        Bitmap resized=null;
        long heap = Debug.getNativeHeapSize();
        while(heap>getBitmapOfWidth(R.drawable.first_zone_map)*getBitmapOfHeight(R.drawable.first_zone_map)){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap src = BitmapFactory.decodeResource(context.getResources(),R.drawable.first_zone_map,options);
            resized = Bitmap.createScaledBitmap(src, 2, 2, true);
        }



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

    /** Get Bitmap's Width **/
    public int getBitmapOfWidth(int drawable){
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(),drawable,options);
            return options.outWidth;
        } catch(Exception e) {
            return 0;
        }
    };

    /** Get Bitmap's height **/
    public int getBitmapOfHeight(int drawable){
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), drawable, options);
            return options.outHeight;
        } catch(Exception e) {
            return 0;
        }
    }


    public void getDataCheckBox(){
    }
    public void setDataCheckBox(){
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

}