package com.sabang.sp.api;

import com.android.volley.Response;

import org.apache.http.entity.ContentType;

/**
 * Created by cyc on 2015-10-29.
 */
public class BoardWriteRequest extends BaseRequest<BaseModel>{

    public static BoardWriteRequest newInstance(byte[] image, String user,String title, String content, String cost, String item,  Response.Listener<BaseModel> listener, Response.ErrorListener errorListener){
        BoardWriteRequest re = new BoardWriteRequest(listener, errorListener);
        re.mBuilder.addBinaryBody("image", image, ContentType.create("image/jpeg"), "AJS.jpeg");
        re.mBuilder.addTextBody("user", user, UTF_8_TEXT_PLAIN);
        re.mBuilder.addTextBody("title", title, UTF_8_TEXT_PLAIN);
        re.mBuilder.addTextBody("content", content, UTF_8_TEXT_PLAIN);
        re.mBuilder.addTextBody("cost", cost, UTF_8_TEXT_PLAIN);
        re.mBuilder.addTextBody("item", item, UTF_8_TEXT_PLAIN);

        return re;
    }


    private BoardWriteRequest(Response.Listener<BaseModel> listener, Response.ErrorListener errorListener) {
        super(Method.POST, "http://hanyang24.vps.phps.kr/board_write/", BaseModel.class, listener, errorListener);
    }


}
/*public File image;

public String user;
public String title;
public String content;
public String cost;
public String item;
public String date;*/