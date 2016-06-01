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
    void showChangeProgress(int xuanzhe,int position);//显示修改订单的Progress
    void removeDindDan(String id,int position, int xuanzhe);//删除数据
    void hideChangeProgress(int xuanzhe,int position);//隐藏改变数据
    void showErrorToast();//显示出错时的toast
    void onChangeFailure(Exception e);
    void onChangeErrorResponse(int xuanzhe,int position);
    void onloadErrorResponse(int xuanzhe);
    void ClearData();
}
