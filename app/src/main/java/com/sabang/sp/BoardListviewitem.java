package com.sabang.sp;

import java.io.Serializable;

/**
 * Created by AJS on 2015-09-28.
 */
public class BoardListviewitem  implements Serializable {
    public int id;
    public int year;
    public int month;
    public int day;
    public int icon;
    public String name;
    public int price;
    public int state; //1 selling 2 sold 3 free
}
