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
    void showProgress(int xuanzhe);
    void resetDindDan(List<OrderInfo> newsList, int xuanzhe);
    void hideProgress(int xuanzhe);
    void showLoadFailMsg();
    void showChangeProgress(ProgressBar pb_button, TextView button);
    void removeDindDan(OrderInfo info, int xuanzhe);
    void hideChangeProgress(ProgressBar pb_button, TextView button);
    void showErrorToast();
    void onChangeFailure(Exception e);
    void onChangeErrorResponse(int xuanzhe,ProgressBar pb_button,TextView button);
    void onloadErrorResponse(int xuanzhe);
    void ClearData();
}
