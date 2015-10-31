package com.sabang.sp.api;

import com.android.volley.Response;

/**
 * Created by cyc on 2015-10-31.
 */
public class CommentRequest extends BaseRequest<BaseModel> {


    public static CommentRequest newInstance(String user, int id, String content, Response.Listener<BaseModel> listener, Response.ErrorListener errorListener){
        CommentRequest re = new CommentRequest(listener, errorListener);
        re.mBuilder.addTextBody("user", user, UTF_8_TEXT_PLAIN);
        re.mBuilder.addTextBody("content", content, UTF_8_TEXT_PLAIN);
        re.mBuilder.addTextBody("board_id", Integer.toString(id), UTF_8_TEXT_PLAIN);

        return re;
    }


    private CommentRequest(Response.Listener<BaseModel> listener, Response.ErrorListener errorListener) {
        super(Method.POST, "http://hanyang24.vps.phps.kr/comment/", BaseModel.class, listener, errorListener);
    }

}
