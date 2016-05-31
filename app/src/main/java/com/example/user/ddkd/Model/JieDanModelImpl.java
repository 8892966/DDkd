package com.example.user.ddkd.Model;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.beam.MainMsgInfo;
import com.example.user.ddkd.beam.QOrderInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by User on 2016-05-26.
 */
public class JieDanModelImpl implements IJieDanModel {

    @Override
    public void CountOrder(final String url, final JieDanListener jieDanListener) {
        MyApplication.getQueue().cancelAll("volley_MSG_GET");
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                try {
                    String ss = (String) o;
                    Log.e("volley_MSG_GET", ss);
                    Gson gson = new Gson();
                    if (ss.startsWith("{")) {
                        MainMsgInfo info = gson.fromJson((String) o, MainMsgInfo.class);
                        jieDanListener.CountOrder_SUCCESS(info);
                    }
                    jieDanListener.CountOrder_NEXT();
                }catch (Exception e){
                    jieDanListener.Exception();
                }
            }

            @Override
            public void tokenouttime() {
                jieDanListener.tokenouttime("com.example.user.ddkd.Model.JieDanModelImpl","CountOrder",url,"com.example.user.ddkd.Model.JieDanModelImpl.JieDanListener",jieDanListener);
            }

            @Override
            public void yidiensdfsdf() {
                jieDanListener.yidiensdfsdf();
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                jieDanListener.onErrorResponse();
            }
        });
        request_post.setTag("volley_MSG_GET");
        MyApplication.getQueue().add(request_post);
    }

    @Override
    public void RobOrder(final String url, final JieDanListener jieDanListener) {
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s = (String) o;
                Log.e("dasdsadas",s);
                if ("SUCCESS".equals(s)) {
                    jieDanListener.ROB_SUCCESS(url);
                } else if("ROB SUCCESS".equals(s)){
                    jieDanListener.ROB_SUCCESS2(url);
                } else if("ROB FAIL".equals(s)){
                    jieDanListener.ROB_ERROR2(url);
                }else{
                    jieDanListener.ROB_ERROR(url);
                }
            }

            @Override
            public void tokenouttime() {
                jieDanListener.tokenouttime("com.example.user.ddkd.Model.JieDanModelImpl","RobOrder",url,"com.example.user.ddkd.Model.JieDanModelImpl.JieDanListener",jieDanListener);
            }

            @Override
            public void yidiensdfsdf() {
                jieDanListener.yidiensdfsdf();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                jieDanListener.onErrorResponse();
            }
        });
        request_post.setTag("volley_QD_GET");
        MyApplication.getQueue().add(request_post);
    }

    @Override
    public void getBespeakOrder(final String url, final JieDanListener jieDanListener) {
        MyApplication.getQueue().cancelAll("volley_GDMSG_GET_UTILS");
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                try {
                    String ss = (String) o;
                    if(!"NODATA".equals(ss)) {
                        Gson gson = new Gson();
                        List<QOrderInfo> list = gson.fromJson(ss, new TypeToken<List<QOrderInfo>>() {
                        }.getType());
                        jieDanListener.GD_MSG_SUCCESS(list);
                    }else {
                        jieDanListener.GD_MSG_NODATA();
                    }
                }catch (Exception e){
                    jieDanListener.Exception();
                }
            }
            @Override
            public void tokenouttime() {
                jieDanListener.tokenouttime("com.example.user.ddkd.Model.JieDanModelImpl","getBespeakOrder",url,"com.example.user.ddkd.Model.JieDanModelImpl.JieDanListener",jieDanListener);
            }
            @Override
            public void yidiensdfsdf() {
                jieDanListener.yidiensdfsdf();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                jieDanListener.onErrorResponse();
            }
        });
        request_post.setTag("volley_GDMSG_GET_UTILS");
        MyApplication.getQueue().add(request_post);
    }

    @Override
    public void RobBespeakOrder(final String url, final JieDanListener jieDanListener) {
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                try {
                    String ss = (String) o;
                    if("ROB SUCCESS".equals(ss)){
                        jieDanListener.GD_ROB_SUCCESS(url);
                    }else if("ROB FAIL".equals(ss)){
                        jieDanListener.GD_ROB_FAIL();
                    }else {
                        jieDanListener.GD_ROB_ERROR();
                    }
                }catch (Exception e){
                    jieDanListener.Exception();
                }
            }
            @Override
            public void tokenouttime() {
                jieDanListener.tokenouttime("com.example.user.ddkd.Model.JieDanModelImpl","RobBespeakOrder",url,"com.example.user.ddkd.Model.JieDanModelImpl.JieDanListener",jieDanListener);
            }
            @Override
            public void yidiensdfsdf() {
                jieDanListener.yidiensdfsdf();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                jieDanListener.onErrorResponse();
            }
        });
        request_post.setTag("volley_QDGD_GET");
        MyApplication.getQueue().add(request_post);
        MyApplication.getQueue().start();
    }

    public interface JieDanListener extends ITokenManage{
        void GD_ROB_SUCCESS(String url);
        void GD_ROB_FAIL();
        void GD_ROB_ERROR();
        void Exception();
        void onErrorResponse();
        void GD_MSG_NODATA();
        void GD_MSG_SUCCESS(List<QOrderInfo> list);
        void ROB_SUCCESS(String url);
        void ROB_ERROR(String url);
        void CountOrder_SUCCESS(MainMsgInfo info);
        void CountOrder_NEXT();

        void ROB_SUCCESS2(String url);

        void ROB_ERROR2(String url);
    }

}
