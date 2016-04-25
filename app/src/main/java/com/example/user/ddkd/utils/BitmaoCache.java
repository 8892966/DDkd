package com.example.user.ddkd.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2016/4/19.
 */
public class BitmaoCache implements ImageLoader.ImageCache {
    public LruCache<String,Bitmap> cache;
    public int max=10*1024*1024;
    public BitmaoCache(){
        cache=new LruCache<String,Bitmap>(max){
            protected int sizeOf(String key,Bitmap values){
                return values.getRowBytes()*values.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String s) {

        return cache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        cache.put(s,bitmap);
    }
}
