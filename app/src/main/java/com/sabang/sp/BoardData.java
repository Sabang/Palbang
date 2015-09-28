package com.sabang.sp;

/**
 * Created by AJS on 2015-09-28.
 */
public class BoardData {
    public int id;
    public int user_id;
    public int year;
    public int month;
    public int day;
    public int state ; //1 selling 2 sold 3 free
    public int icon;
    public String name;
    public int price;


    public BoardData(int id, int user_id, int year, int month, int day, int state, int Image, String name, int price){
        this.id = id;
        this.user_id = user_id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.state = state; //1 selling 2 sold 3 free
        icon = Image;
        this.name = name;
        this.price = price;
    }
}
