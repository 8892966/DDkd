package com.example.user.ddkd.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.user.ddkd.Model.ZhuCeModelImpl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class UploadUtil1 {

    public void uploadMethod(final RequestParams params, final String uploadHost, final ZhuCeModelImpl.SSubmitPicturesListener sSubmitPicturesListener) {
        try {
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, uploadHost, params, new RequestCallBack<String>() {

                @Override
                public void onStart() {
//                    Log.e("ZhuCe4Activity", "开始");
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    if (isUploading) {
                        Log.e("ZhuCe4Activity", "upload: " + current + "/" + total);
                    } else {
                        Log.e("ZhuCe4Activity", "upload: " + current + "/" + total);
                    }
                    sSubmitPicturesListener.onLoading(total,current,isUploading);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("ZhuCe4Activity", "reply: " + responseInfo.result);
                        if ("SUCCESS".equals(responseInfo.result)) {
                            sSubmitPicturesListener.SUCCESS();
                        } else if ("MAXSIZE OUT".equals(responseInfo.result)) {
                            sSubmitPicturesListener.MAXSIZE_OUT();
                        } else if ("UPLOAD FILE FORMAT ERROR".equals(responseInfo.result)) {
                            sSubmitPicturesListener.UPLOAD_FILE_FORMAT_ERROR();
                        } else if ("UPLOAD FAIL".equals(responseInfo.result)) {
                            sSubmitPicturesListener.UPLOAD_FAIL();
                        } else {
                            sSubmitPicturesListener.ERROR();
                        }
                }
                @Override
                public void onFailure(HttpException error, String msg) {
                    sSubmitPicturesListener.onFailure();
                }
            });
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
            sSubmitPicturesListener.onException();
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
