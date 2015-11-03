package com.sabang.sp.api;

import com.android.volley.Response;

/**
 * Created by cyc on 2015-11-03.
 */

/*
* id를 준다
* */

public class BoardDeleteRequest extends BaseRequest<BoardDeleteRequest.Model>{

    public static BoardDeleteRequest newInstance(int id, Response.Listener<Model> listener, Response.ErrorListener errorListener) {
        return new BoardDeleteRequest( String.format("http://hanyang24.vps.phps.kr/board/%1$s/delete", id),listener, errorListener);
    }

    private BoardDeleteRequest(String url,Response.Listener<Model> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, Model.class, listener, errorListener);
    }

    public static class Model {
        public BoardModel boardDetail;
    }

}
