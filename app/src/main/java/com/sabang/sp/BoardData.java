package com.sabang.sp;

import java.io.Serializable;

/**
 * Created by AJS on 2015-09-28.
 */
public class BoardData implements Serializable{
    public int id;
    public String email;
    public int year;
    public int month;
    public int day;
    public int state ; //1 selling 2 sold 3 free
    public int icon;
    public String name;
    public int price;
    public String title;


    public BoardData(int id, String email, int year, int month, int day, int state, int Image, String name, int price,String title){
        this.id = id;
        this.email = email;
        this.year = year;
        this.month = month;
        this.day = day;
        this.state = state; //1 selling 2 sold 3 free
        icon = Image;
        this.name = name;
        this.price = price;
        this.title = title;
    }
}
