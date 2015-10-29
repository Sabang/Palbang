package com.sabang.sp.api;

import java.io.Serializable;

/**
 * Created by cyc on 2015-10-29.
 */
public class BoardModel extends BaseModel implements Serializable {

    public String[] images;
    public UserModel users;
    public String title;
    public String content;
    public String cost;
    public String item;
    public String date;
}
