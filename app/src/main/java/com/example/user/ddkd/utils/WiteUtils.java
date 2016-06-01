package com.example.user.ddkd.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.example.user.ddkd.Activity_problem;
import com.example.user.ddkd.MainActivity_getmoney;

/**
 * Created by Administrator on 2016/6/1.
 */
public class WiteUtils {
    private ProgressDialog progressDialog;
    private Activity activity;
    public WiteUtils(Activity activity){
        this.activity=activity;
    }
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("正在提交........");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
