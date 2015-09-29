package com.sabang.sp.api;

import com.android.volley.Response;

import java.util.List;

/**
 * Created by yoo2 on 15. 9. 20..
 */
public class RoomRequest extends BaseRequest<RoomRequest.Model>{

    public static RoomRequest newInstance(Response.Listener<Model> listener, Response.ErrorListener errorListener) {
        return new RoomRequest(listener, errorListener);
    }

    private RoomRequest(Response.Listener<Model> listener, Response.ErrorListener errorListener) {
        super(Method.GET, "http://hanyang24.vps.phps.kr/room/", Model.class, listener, errorListener);
    }

    public static class Model {
        public List<RoomModel> rooms;
    }
}
