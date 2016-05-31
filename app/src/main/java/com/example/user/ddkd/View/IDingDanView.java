package com.example.user.ddkd.View;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.user.ddkd.beam.OrderInfo;

import java.util.List;

/**
 * Created by User on 2016-05-13.
 */
public interface IDingDanView {
    void showProgress(int xuanzhe);//显示Progress
    void resetDindDan(List<OrderInfo> newsList, int xuanzhe);//刷新数据
    void hideProgress(int xuanzhe);//隐藏Progress
    void showLoadFailMsg();
    void showChangeProgress(ProgressBar pb_button, TextView button);//显示修改订单的Progress
    void removeDindDan(OrderInfo info, int xuanzhe);//删除数据
    void hideChangeProgress(ProgressBar pb_button, TextView button);//隐藏改变数据
    void showErrorToast();//显示出错时的toast
    void onChangeFailure(Exception e);
    void onChangeErrorResponse(int xuanzhe,ProgressBar pb_button,TextView button);
    void onloadErrorResponse(int xuanzhe);
    void ClearData();
}
