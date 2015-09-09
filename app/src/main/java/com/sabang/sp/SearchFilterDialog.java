package com.sabang.sp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;

/**
 * Created by cyc on 2015-09-06.
 */
public class SearchFilterDialog extends DialogFragment implements View.OnClickListener
{
    private Button acceptButton;
    private Button cancelButton;
    private CheckBox checkBox_A;
    private CheckBox checkBox_B;
    private CheckBox checkBox_C;
    private CheckBox checkBox_D;
    private CheckBox checkBox_E;
    private CheckBox checkBox_F;
    private Spinner spinner_security_min;
    private Spinner spinner_security_max;
    private Spinner spinner_monthly_min;
    private Spinner spinner_monthly_max;


    SearchFilterData data;

    public SearchFilterDialog()
    {
        // Empty constructor required for DialogFragment
    }

    public void setSearchFilterData(SearchFilterData data){
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_search_filter, container);

        getDialog().setTitle(R.string.title_dialog_search_finter);



        checkBox_A =  (CheckBox) view.findViewById(R.id.dialogfragment_checkBox_A);
        checkBox_B =  (CheckBox) view.findViewById(R.id.dialogfragment_checkBox_B);
        checkBox_C =  (CheckBox) view.findViewById(R.id.dialogfragment_checkBox_C);
        checkBox_D =  (CheckBox) view.findViewById(R.id.dialogfragment_checkBox_D);
        checkBox_E =  (CheckBox) view.findViewById(R.id.dialogfragment_checkBox_E);
        checkBox_F =  (CheckBox) view.findViewById(R.id.dialogfragment_checkBox_F);
        getDataCheckBox();




        spinner_security_min = (Spinner) view.findViewById(R.id.dialogfragment_spinner_security_min);
        spinner_security_max = (Spinner) view.findViewById(R.id.dialogfragment_spinner_security_max);
        spinner_monthly_min = (Spinner) view.findViewById(R.id.dialogfragment_spinner_monthly_min);
        spinner_monthly_max = (Spinner) view.findViewById(R.id.dialogfragment_spinner_monthly_max);
        spinner_security_min.setAdapter(ArrayAdapter.createFromResource(
                view.getContext(), R.array.security_money, android.R.layout.simple_spinner_dropdown_item));
        spinner_security_max.setAdapter(ArrayAdapter.createFromResource(
                view.getContext(), R.array.security_money, android.R.layout.simple_spinner_dropdown_item));
        spinner_monthly_min.setAdapter(ArrayAdapter.createFromResource(
                view.getContext(), R.array.monthly_money, android.R.layout.simple_spinner_dropdown_item));
        spinner_monthly_max.setAdapter(ArrayAdapter.createFromResource(
                view.getContext(), R.array.monthly_money, android.R.layout.simple_spinner_dropdown_item));
        getDataSpinner();





        acceptButton = (Button) view.findViewById(R.id.dialogfragment_acceptbtn);
        cancelButton = (Button) view.findViewById(R.id.dialogfragment_canceltbtn);
        acceptButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v)
    {
        if (v == acceptButton)
        {
            setDataCheckBox();
            setDataSpinner();

            this.dismiss();
        }
        else if (v == cancelButton)
        {
            this.dismiss();
        }
    }

    public void getDataCheckBox(){
        checkBox_A.setChecked(data.check_A);
        checkBox_B.setChecked(data.check_B);
        checkBox_C.setChecked(data.check_C);
        checkBox_D.setChecked(data.check_D);
        checkBox_E.setChecked(data.check_E);
        checkBox_F.setChecked(data.check_F);
    }
    public void setDataCheckBox(){
        data.check_A = checkBox_A.isChecked();
        data.check_B = checkBox_B.isChecked();
        data.check_C = checkBox_C.isChecked();
        data.check_D = checkBox_D.isChecked();
        data.check_E = checkBox_E.isChecked();
        data.check_F = checkBox_F.isChecked();
    }
    public void getDataSpinner(){
        String[] array_security = getResources().getStringArray(R.array.security_money);
        String[] array_monthly =getResources().getStringArray(R.array.monthly_money);

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
        String securityFirstElement = getResources().getStringArray(R.array.security_money)[0];
        String monthlyFristElement = getResources().getStringArray(R.array.monthly_money)[0];

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