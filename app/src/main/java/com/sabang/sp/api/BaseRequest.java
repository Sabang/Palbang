package com.sabang.sp.api;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class BaseRequest<T> extends Request<T> {
    public static final ContentType UTF_8_TEXT_PLAIN = ContentType.create(
            "text/plain", MIME.UTF8_CHARSET);

    private final Class<T> mClazz;
    private final Response.Listener<T> mListener;
    private HttpEntity mHttpEntity;

    protected final GsonBuilder mGsonBuilder;
    protected final MultipartEntityBuilder mBuilder;


    public BaseRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        mClazz = clazz;
        mListener = listener;

        mGsonBuilder = new GsonBuilder().setFieldNamingPolicy(
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        mBuilder = MultipartEntityBuilder.create();

        setRetryPolicy(new DefaultRetryPolicy(60000, 0, 1));
    }

    public void send(RequestQueue queue) {
        queue.add(this);
    }

    public void send() {
        send(VolleySingleton.getInstance().getRequestQueue());
    }

    public HttpEntity getHttpEntity() {
        if (mHttpEntity == null) {
            mHttpEntity = mBuilder.build();
        }

        return mHttpEntity;
    }

    @Override
    public String getBodyContentType() {
        return getHttpEntity().getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            getHttpEntity().writeTo(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    public void deliverError(VolleyError error) {
//        if (error != null && error.networkResponse != null
//                && error.networkResponse.statusCode == 401) {
//        } else {
        super.deliverError(error);
//        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.i("SP", getUrl() + "\n" + json);
            return Response.success(mGsonBuilder.create().fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (mListener != null && response != null) {
            mListener.onResponse(response);
        }
    }
}
