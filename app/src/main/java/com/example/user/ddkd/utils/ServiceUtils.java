package com.example.user.ddkd.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by User on 2016-04-20.
 */
public class ServiceUtils {
    private ServiceUtils(){

    }
    public static boolean isRunning(Context context,String servicename){
        ActivityManager manager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo s:runningServices){
            if(servicename.equals(s.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
