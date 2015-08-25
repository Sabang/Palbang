package com.sabang.sp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.Serializable;
import java.io.SerializablePermission;
import java.util.ArrayList;

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
