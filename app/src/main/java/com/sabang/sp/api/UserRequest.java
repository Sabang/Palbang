package com.sabang.sp.api;

import com.android.volley.Response;

/**
 * Created by cyc on 2015-10-11.
 */
public class UserRequest extends BaseRequest<BaseModel> {

    public static UserRequest newInstance(String userId, Response.Listener<BaseModel> listener, Response.ErrorListener errorListener){
        UserRequest re = new UserRequest(listener, errorListener);
        re.mBuilder.addTextBody("user_id", userId, UTF_8_TEXT_PLAIN);
        return re;
    }

    private UserRequest(Response.Listener<BaseModel> listener, Response.ErrorListener errorListener) {
        super(Method.POST, "http://hanyang24.vps.phps.kr/user/", BaseModel.class, listener, errorListener);
    }
}
