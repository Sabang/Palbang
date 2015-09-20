package com.sabang.sp;

/**
 * Created by cyc on 2015-09-06.
 */
public class SearchFilterData {

    //go to array
    public boolean check_A;
    public boolean check_B;
    public boolean check_C;
    public boolean check_D;
    public boolean check_E;
    public boolean check_F;


    //will delete.
    public boolean isSecurity_min;
    public boolean isSecurity_max;
    public boolean isMonthly_min;
    public boolean isMonthly_max;

    public String security_min;
    public String security_max;
    public String montly_min;
    public String montly_max;

    public SearchFilterData(){
        check_A = true;
        check_B = true;
        check_C = true;
        check_D = true;
        check_E = true;
        check_F = true;

        isMonthly_min = false;
        isMonthly_max = false;
        isSecurity_min = false;
        isSecurity_max = false;
    }
}