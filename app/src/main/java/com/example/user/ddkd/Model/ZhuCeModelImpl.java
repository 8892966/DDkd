package com.example.user.ddkd.Model;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.utils.UploadUtil1;
import com.lidroid.xutils.http.RequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-05-20.
 */
public class ZhuCeModelImpl implements IZhuCeModel{

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
//                    tv_button_yanzhengma.setText("验证码");
                    if ("SUCCESS".equals(s)) {
                        sSubmitPicturesListener.phoisExist();
//                        tv_button_yanzhengma.setEnabled(true);
                    } else {
                        sSubmitPicturesListener.phoNotExist();
//                        tv_button_yanzhengma.setEnabled(false);
//                        Toast.makeText(getActivity(), "用户已存在！", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    sSubmitPicturesListener.phoExistonErrorResponse();
//                    tv_button_yanzhengma.setText("验证码");
//                    tv_button_yanzhengma.setEnabled(false);
//                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            });
            request_post.setTag("volley_phoExist_GET");
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
}
