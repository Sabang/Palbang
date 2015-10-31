package com.sabang.sp.api;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by cyc on 2015-10-29.
 */
public class BoardModel extends BaseModel implements Serializable {

    public String[] images;

    public ArrayList<CommentModel> comments;
    public String user;
    public String title;
    public String content;
    public String cost;
    public String item;
    public String date;

    public int id;
}
