package com.example.user.ddkd.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.user.ddkd.ExitApplication;
import com.example.user.ddkd.MainActivity_login;
import com.example.user.ddkd.MyApplication;
import com.tencent.android.tpush.XGPushManager;

/**
 * Created by User on 2016-04-26.
 */
public class Exit {
    public static void exit(Activity activity) {
        SharedPreferences sharedPreferences=activity.getSharedPreferences("config", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("loginstatic","0");
        MyApplication.state=0;
        editor.commit();
        ExitApplication.getInstance().exit();
        Intent intent=new Intent(activity,MainActivity_login.class);
        activity.startActivity(intent);
        XGPushManager.unregisterPush(activity);
        activity.finish();
        Toast.makeText(activity, "退出成功", Toast.LENGTH_SHORT).show();
    }
}
