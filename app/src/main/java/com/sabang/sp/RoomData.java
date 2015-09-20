package com.sabang.sp;

/**
 * Created by cyc on 2015-09-10.
 */
public class RoomData {
    public int id;
    public int area;
    public int security;
    public int monthly;
    public int icon;


    public RoomData(int id, int area, int security, int monthly, int firstImage){
        this.id = id;
        this.area = area;
        this.security = security;
        this.monthly = monthly;

        icon = firstImage;
    }

}
