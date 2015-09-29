package com.sabang.sp;

import java.io.Serializable;

/**
 * Created by James on 2015-08-23.
 */
public class BoardModel implements Serializable {
    private String title;
    private String content;

    public BoardModel(String title, String content){
        this.title = title;
        this.content = content;
    }

    public String toString(){

        return (title+" "+content);
    }


}
