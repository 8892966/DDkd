package com.example.user.ddkd;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ExitApplication{
    private Set<Activity> activityList = new HashSet<>();
    private static ExitApplication instance;
    private ExitApplication() {

    }

    // 单例模式中获取唯一的ExitApplication实例
    public static ExitApplication getInstance() {
        if (null == instance) {
            instance = new ExitApplication();
        }
        return instance;

    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if(activityList==null){
            activityList=new HashSet<>();
        }
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        int siz = activityList.size();
        Activity[] activities= activityList.toArray(new Activity[siz]);
        for(Activity a:activities){
            if (a != null) {
                Log.e("Activity:", a.toString());
                a.finish();
            }
        }
        activityList.clear();
        activityList=null;
//        for (int i = 0; i < siz; i++) {
//            if (activityList != null) {
//                Log.e("Activity:", activityList.get(i).toString());
//                ((Activity) activityList.get(i)).finish();
//            }
//        }finish
    }

}