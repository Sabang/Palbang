package com.sabang.sp.api;

import com.android.volley.Response;

/**
 * Created by cyc on 2015-10-31.
 */
public class BoardDetailRequest extends BaseRequest<BoardDetailRequest.Model>{

    public static BoardDetailRequest newInstance(int id, Response.Listener<Model> listener, Response.ErrorListener errorListener) {
        return new BoardDetailRequest( String.format("http://hanyang24.vps.phps.kr/board/%1$s/detail", id),listener, errorListener);
    }

    private BoardDetailRequest(String url,Response.Listener<Model> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, Model.class, listener, errorListener);
    }

    public static class Model {
        public BoardModel boardDetail;
    }
}
