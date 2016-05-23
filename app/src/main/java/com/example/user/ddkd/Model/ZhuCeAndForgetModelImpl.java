package com.example.user.ddkd.Model;

import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.utils.UploadUtil1;
import com.lidroid.xutils.http.RequestParams;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-05-20.
 */
public class ZhuCeAndForgetModelImpl implements IZhuCeAndForgetModel {
    @Override
    public void SubmitData(final Map<String, String> map, final SSubmitPicturesListener sSubmitPicturesListener) {
            String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/register";
            StringRequest request_post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if ("SUCCESS".equals(s)) {
                        sSubmitPicturesListener.DateSUCCESS();
                    } else {
                        sSubmitPicturesListener.DateERROR();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    sSubmitPicturesListener.ErrorListener();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map1 = new HashMap<>();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        Log.e("getParams",entry.getKey()+":"+entry.getValue());
                        map1.put(entry.getKey(), entry.getValue());
                    }
                    return map1;
                }
            };
        request_post.setTag("volley_ZC_GET");
        MyApplication.getQueue().add(request_post);
    }

    @Override
    public void phoExist(String phone, final SSubmitPicturesListener sSubmitPicturesListener) {
            String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/phoExist/phone/" + phone;
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
            StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.e("volley_phoExist_GET", s);
                    sSubmitPicturesListener.phoExist();
                    if ("SUCCESS".equals(s)) {
                        sSubmitPicturesListener.phoisExist();
                    } else {
                        sSubmitPicturesListener.phoNotExist();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    sSubmitPicturesListener.phoExistonErrorResponse();
                }
            });
            request_post.setTag("volley_phoExist_GET");
            MyApplication.getQueue().add(request_post);
    }

    @Override
    public void modifyPsw(final ForgetListener forgetListener,String phone) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/modifyPsw/phone/"+phone;
        final StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("volley_getYZM_GET",s);
                if("ERROR".equals(s)){
                    forgetListener.Error();
//                    Toast.makeText(MainActivity_forget.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
                }else if("phone no exists".equals(s)){
                    forgetListener.Phone_No_Exists();
                }else {
                    try {
                        InputStream is = new ByteArrayInputStream(s.getBytes("utf-8"));
                        XmlPullParser parser = Xml.newPullParser();
                        parser.setInput(is, "UTF-8");
                        int eventType = parser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            switch (eventType) {
                                case XmlPullParser.START_TAG:
                                    if (parser.getName().equals("code")) {
                                        eventType = parser.next();
                                        String code = parser.getText();
                                        if ("2".equals(code)) {
                                            forgetListener.Success();
//                                            Log.i("ZhuCe1Activity", "请留意您的短信");
//                                            Toast.makeText(MainActivity_forget.this, "请留意您的短信", Toast.LENGTH_SHORT).show();
                                        } else if ("4085".equals(code)) {
                                            forgetListener.Out_Of_Range();
//                                            Log.i("ZhuCe1Activity", "同一手机号验证码短信发送超出5条");
//                                            Toast.makeText(MainActivity_forget.this, "同一手机号验证码短信发送超出5条", Toast.LENGTH_SHORT).show();
                                        } else {
                                            forgetListener.Failure();
//                                            Toast.makeText(MainActivity_forget.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
//                                            Log.i("ZhuCe1Activity", "获取验证码失败");
                                        }
                                    }
                                    break;
                            }
                            eventType = parser.next();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                forgetListener.onErrorResponse();
//                Toast.makeText(MainActivity_forget.this,"网络连接中断",Toast.LENGTH_SHORT).show();
            }
        });
        request_post.setTag("volley_getYZM_GET");
        MyApplication.getQueue().add(request_post);
    }

    @Override
    public void SubmitPictures(String name,String phone,File file,SSubmitPicturesListener sSubmitPicturesListener) {
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("name", name);
        requestParams.addBodyParameter("phone", phone);
        requestParams.addBodyParameter("file", file);
        new UploadUtil1().uploadMethod(requestParams, "http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage",sSubmitPicturesListener);
    }



    public interface SSubmitPicturesListener{

        void onFailure();

        void onException();

        void SUCCESS();

        void MAXSIZE_OUT();

        void UPLOAD_FILE_FORMAT_ERROR();

        void UPLOAD_FAIL();

        void onLoading(long total, long current, boolean isUploading);

        void ERROR();

        void DateSUCCESS();

        void DateERROR();

        void ErrorListener();

        void phoExist();

        void phoisExist();

        void phoNotExist();

        void phoExistonErrorResponse();

    }
    public interface ForgetListener{
        void Error();
        void Phone_No_Exists();
        void Success();
        void Out_Of_Range();
        void Failure();
        void onErrorResponse();
    }
}
