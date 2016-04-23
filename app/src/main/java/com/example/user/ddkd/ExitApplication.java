package com.example.user.ddkd;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ExitApplication{
    private List activityList = new LinkedList();
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
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {

        int siz = activityList.size();
        for (int i = 0; i < siz; i++) {
            if (activityList.get(i) != null) {
                ((Activity) activityList.get(i)).finish();
            }
        }

    }

}