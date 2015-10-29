package com.sabang.sp.api;

import com.android.volley.Response;

import java.util.List;

/**
 * Created by cyc on 2015-10-30.
 */
public class BoardRequest extends BaseRequest<BoardRequest.Model>{

    public static BoardRequest newInstance(Response.Listener<Model> listener, Response.ErrorListener errorListener) {
        return new BoardRequest(listener, errorListener);
    }

    private BoardRequest(Response.Listener<Model> listener, Response.ErrorListener errorListener) {
        super(Method.GET, "http://hanyang24.vps.phps.kr/board/", Model.class, listener, errorListener);
    }

    public static class Model {
        public List<BoardModel> boards;
    }
}
