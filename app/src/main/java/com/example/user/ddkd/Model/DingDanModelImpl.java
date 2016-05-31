package com.example.user.ddkd.Model;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.beam.OrderInfo;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016-05-13.
 */
public class DingDanModelImpl implements IDingDanModel {

    @Override
    public void loadDingDins(final String url, final DingDanModelImpl.OnloadDingDinsListListener listener) {
        MyApplication.getQueue().cancelAll("volley_getOrder_GET");
            StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s = (String) o;
                try {
                    List<OrderInfo> list = new ArrayList<>();
                    if (!"ERROR".equals(s)) {
                        Gson gson = new Gson();
                        list = gson.fromJson((String) o, new TypeToken<List<OrderInfo>>() {
                        }.getType());
                        //转化时间戳
                        for (OrderInfo info : list) {
                            info.setTime(TimeUtil.getStrTime(info.getTime()));
                            info.setOrderTime(TimeUtil.getStrTime(info.getOrderTime()));
                        }
                    }
                    listener.onloadSuccess(list,url);
                } catch (Exception e) {
                    listener.onloadFailure("", e,url);
                }
            }

            @Override
            public void tokenouttime() {
                listener.tokenouttime("com.example.user.ddkd.Model.DingDanModelImpl","loadDingDins",url,"com.example.user.ddkd.Model.DingDanModelImpl.OnloadDingDinsListListener",listener);
            }

            @Override
            public void yidiensdfsdf() {
                listener.yidiensdfsdf();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onloadErrorResponse(volleyError,url);
            }
        });
        request_post.setTag("volley_getOrder_GET");
        MyApplication.getQueue().add(request_post);
    }

    @Override
    public void ChangeDingDins(final String url, final DingDanModelImpl.OnChangeDingDinsListListener listener) {
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s = (String) o;
                try {
                    if ("SUCCESS".equals(s)) {
                        listener.onChangeSuccess(url);
                    } else {
                        listener.onChangeError(url);
                    }
                } catch (Exception e) {
                    listener.onChangeFailure("", e,url);
                }
            }

            @Override
            public void tokenouttime() {
                listener.tokenouttime("com.example.user.ddkd.Model.DingDanModelImpl","ChangeDingDins",url,"com.example.user.ddkd.Model.DingDanModelImpl.OnChangeDingDinsListListener",listener);
            }

            @Override
            public void yidiensdfsdf() {
                listener.yidiensdfsdf();
           }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onChangeErrorResponse(volleyError,url);
            }
        });
        request_post.setTag("volley_OrderState_GET");
        MyApplication.getQueue().add(request_post);
    }

    public interface OnloadDingDinsListListener extends ITokenManage{
        void onloadSuccess(List<OrderInfo> list, String url);
        void onloadFailure(String msg, Exception e, String url);
        void onloadErrorResponse(VolleyError volleyError, String url);

    }

    public interface OnChangeDingDinsListListener extends ITokenManage{
        void onChangeSuccess(String url);
        void onChangeError(String url);
        void onChangeFailure(String msg, Exception e,String url);
        void onChangeErrorResponse(VolleyError volleyError, String url);
    }

}
