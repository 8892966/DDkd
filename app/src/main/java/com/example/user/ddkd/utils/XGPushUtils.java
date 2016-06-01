package com.example.user.ddkd.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

/**
 * Created by User on 2016-05-25.
 */
public class XGPushUtils {
    public static void StartXGPush( final Context context, final XGPushListener xgPushListener) {
        /**
         * 启动并注册APP，这里拿到的XGtoken是唯一的
         */
        XGPushManager.registerPush(context, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                SharedPreferences preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("XGtoken", (String) data);
                edit.commit();
                xgPushListener.onSuccess();
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                xgPushListener.onFail();
            }
        });
    }
    public static void StopXGPush(Context context){
        /**
         * 反注册信鸽，不再接收来自信鸽推送的内容
         */
        XGPushManager.unregisterPush(context);
        SharedPreferences preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("XGtoken","");
        edit.commit();
    }

    public interface XGPushListener{
        void onSuccess();
        void onFail();
    }
}
