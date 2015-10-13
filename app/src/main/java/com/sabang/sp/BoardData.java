package com.sabang.sp;

import java.io.Serializable;

/**
 * Created by AJS on 2015-09-28.
 */
public class BoardData implements Serializable{
    public int id;
    public String email;
    public String date;
    public int icon;
    public String name;
    public String price;
    public String title;
    public String contents;


    public BoardData(int id, String email, String date, int Image, String name, String price,String title,String contents){
        this.id = id;
        this.email = email;
        this.date = date;
        this.icon = Image;
        this.name = name;
        this.price = price;
        this.title = title;
        this.contents = contents;
    }
}
