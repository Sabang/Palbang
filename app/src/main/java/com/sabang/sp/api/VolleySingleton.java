package com.sabang.sp.api;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.util.LruCache;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.sabang.sp.SPApp;

import java.io.File;

public class VolleySingleton {
    private static final VolleySingleton mInstance = new VolleySingleton();
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    public static VolleySingleton getInstance() {
        return mInstance;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    VolleySingleton() {
        super();

        mRequestQueue = Volley.newRequestQueue(SPApp.getInstance());
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache());
    }

    public static RequestQueue createSingleThreadRequestQueue() {
        Context context = SPApp.getInstance();
        File cacheDir = new File(context.getCacheDir(), "volley");
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        HttpStack stack = Build.VERSION.SDK_INT >= 9 ?
                new HurlStack() :
                new HttpClientStack(AndroidHttpClient.newInstance(userAgent));

        Network network = new BasicNetwork(stack);
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network, 1);
        queue.start();

        return queue;
    }

    public static class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

        public BitmapLruCache() {
            this(getDefaultLruCacheSize());
        }

        public BitmapLruCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }

        public static int getDefaultLruCacheSize() {
            final int maxMemory =
                    (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;

            return cacheSize;
        }

        @Override
        public Bitmap getBitmap(String key) {
            return get(key);
        }

        @Override
        public void putBitmap(String key, Bitmap bitmap) {
            put(key, bitmap);
        }
    }
}
