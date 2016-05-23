package com.example.user.ddkd.utils;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.MyApplication;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by User on 2016-05-23.
 */
public class MyvolleyUtils {

    public void volley_String_get(final Volley_String_get_Listener volley_string_get_listener,String url, final String charset){
        try {
            MyApplication.getQueue().cancelAll("get");
            StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                public void onResponse(String s) {
                    if (volley_string_get_listener != null) {
                        volley_string_get_listener.onResponse();
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    if (volley_string_get_listener != null) {
                        volley_string_get_listener.ErrorListener();
                    }
                }
            }) {
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (!TextUtils.isEmpty(charset)) {
                        String parsed;
                        try {
                            parsed = new String(response.data, charset);
                        } catch (UnsupportedEncodingException e) {
                            parsed = new String(response.data);
                        }
                        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                    } else {
                        return super.parseNetworkResponse(response);
                    }
                }
            };
            request_post.setTag("get");
            MyApplication.getQueue().add(request_post);
        }catch (Exception e){
        }
    }

    public interface Volley_String_get_Listener{
        void onResponse();
        void ErrorListener();
    }

    public void volley_Post(final Volley_String_post_Listener volley_string_post_listener,String url, final Map<String, String> map) {
        try {
            MyApplication.getQueue().cancelAll("post");
            StringRequest request_post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (volley_string_post_listener!=null) {
                        volley_string_post_listener.onResponse();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (volley_string_post_listener!=null) {
                        volley_string_post_listener.ErrorListener();
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    if (map==null){
                        return super.getParams();
                    }
                    return map;
                }

            };
            request_post.setTag("post");
            MyApplication.getQueue().add(request_post);
        }catch (Exception e){
        }
    }
    public interface Volley_String_post_Listener{
        void onResponse();
        void ErrorListener();
    }
}
