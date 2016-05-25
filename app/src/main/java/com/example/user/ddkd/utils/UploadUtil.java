package com.example.user.ddkd.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.ddkd.MainActivity_userinfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class UploadUtil {
    public void uploadMethod(final RequestParams params, final String uploadHost, final Handler handler, final ProgressBar progressBar2, final Context context,final Handler handler2) {
        try {

            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, uploadHost, params, new RequestCallBack<String>() {

                @Override
                public void onStart() {
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    if (progressBar2 != null) {
                        int t;
                        int c;
                        if (total > 1000) {
                            t = (int) (total / 1000);
                        } else {
                            t = (int) total;
                        }
                        if (current > 1000) {
                            c = (int) (current / 1000);
                        } else {
                            c = (int) current;
                        }
                        progressBar2.setMax(t);
                        progressBar2.setProgress(c);
                    }
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (handler2 != null) {
                        Message message = new Message();
                        message.obj = responseInfo.result;
                        message.what = MainActivity_userinfo.REPLY;
                        handler2.sendMessage(message);
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    Toast.makeText(context, "提交超时，请重新提交", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
            Toast.makeText(context,"信息有误!!!",Toast.LENGTH_SHORT).show();
        }
        }

    public static Bitmap getBitmap(String path, BitmapFactory.Options options, float maxH, float maxW) {
        Bitmap cameraBitmap = null;
        int be = 1;
        float maxh = (int) ((maxH / 100.0) + 0.5) * 100;
        float maxw = (int) ((maxW / 100.0) + 0.5) * 100;
        Log.e("maxh", maxh + "");
        Log.e("maxw", maxw + "");
        options.inJustDecodeBounds = true;
        cameraBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        int h = options.outHeight;
        int w = options.outWidth;
        Log.e("h", h + "");
        Log.e("w", w + "");
        if (h > maxh || w > maxw) {
            int beh = (int) ((h / maxh) + 0.5);
            int bew = (int) ((w / maxw) + 0.5);
            Log.e("beh", beh + "");
            Log.e("bew", bew + "");
            if (beh > bew) {
                be = beh;
            } else {
                be = bew;
            }
        }
        options.inSampleSize = be;
        cameraBitmap = BitmapFactory.decodeFile(path, options);
        return cameraBitmap;
    }

}
